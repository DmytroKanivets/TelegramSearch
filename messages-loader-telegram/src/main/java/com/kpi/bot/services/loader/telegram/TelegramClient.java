package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.data.User;
import com.kpi.bot.exceptions.ChannelNotFoundException;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.loader.MessageLoader;
import com.kpi.bot.services.loader.telegram.core.ChatUpdatesBuilderImpl;
import com.kpi.bot.services.loader.telegram.core.CustomUpdatesHandler;
import com.kpi.bot.services.loader.telegram.database.TelegramDatabase;
import com.kpi.bot.services.loader.telegram.exceptions.ChannelAlreadyJoinedException;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import com.kpi.bot.services.loader.telegram.structure.PendingChannels;
import com.kpi.bot.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.api.channel.TLChannelParticipant;
import org.telegram.api.channel.TLChannelParticipants;
import org.telegram.api.channel.participants.TLAbsChannelParticipant;
import org.telegram.api.channel.participants.TLChannelParticipantCreator;
import org.telegram.api.channel.participants.TLChannelParticipantModerator;
import org.telegram.api.channel.participants.filters.TLAbsChannelParticipantsFilter;
import org.telegram.api.channel.participants.filters.TLChannelParticipantsFilterAdmins;
import org.telegram.api.chat.TLAbsChat;
import org.telegram.api.chat.TLChat;
import org.telegram.api.chat.channel.TLChannel;
import org.telegram.api.contacts.TLResolvedPeer;
import org.telegram.api.engine.RpcException;
import org.telegram.api.functions.channels.TLRequestChannelsGetParticipant;
import org.telegram.api.functions.channels.TLRequestChannelsGetParticipants;
import org.telegram.api.functions.channels.TLRequestChannelsJoinChannel;
import org.telegram.api.functions.contacts.TLRequestContactsResolveUsername;
import org.telegram.api.functions.messages.TLRequestMessagesGetAllChats;
import org.telegram.api.functions.messages.TLRequestMessagesGetHistory;
import org.telegram.api.functions.messages.TLRequestMessagesImportChatInvite;
import org.telegram.api.input.chat.TLInputChannel;
import org.telegram.api.input.peer.TLInputPeerChannel;
import org.telegram.api.input.user.TLInputUser;
import org.telegram.api.message.TLAbsMessage;
import org.telegram.api.message.TLMessage;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.updates.TLAbsUpdates;
import org.telegram.api.updates.TLUpdateShortMessage;
import org.telegram.api.updates.TLUpdates;
import org.telegram.bot.kernel.TelegramBot;
import org.telegram.bot.structure.LoginStatus;
import org.telegram.tl.TLIntVector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
public class TelegramClient implements MessageLoader {

    public static String SELF = "130666341";

    private TelegramUpdatesHandler updatesHandler;
    private TelegramApiHandler apiHandler;
    private TelegramConfiguration configuration;
    private TelegramBot kernel;

    private MessageService messageService;

    private Repository<User> userRepository;
    private Repository<Channel> channelRepository;

    private PendingChannels pendingChannels = new PendingChannels();
    private Set<String> joinedChannels = new HashSet<String>() {{
        this.add(SELF);
    }};

    public void stopIndexing(String id) {
        joinedChannels.remove(id);
    }

    public TelegramClient(TelegramConfiguration configuration, MessageService messageService, Repository<User> userRepository, Repository<Channel> channelRepository) {
        this.configuration = configuration;
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;

        updatesHandler = new UpdatesHandler();
        apiHandler = new ApiHandler();

    }

    public TelegramUpdatesHandler getUpdatesHandler() {
        return updatesHandler;
    }

    public TelegramApiHandler getApiHandler() {
        return apiHandler;
    }

    private void saveChannelParticipants(TLChannel tlChannel, TLAbsChannelParticipantsFilter filter) throws ExecutionException, RpcException {
        final int loadSize = 100;

        int currentOffset = 0;
        TLChannelParticipants participants;
        do {
            TLRequestChannelsGetParticipants request = new TLRequestChannelsGetParticipants();
            request.setOffset(currentOffset * loadSize);
            request.setLimit(loadSize);
            TLInputChannel channelRequest = new TLInputChannel();
            channelRequest.setChannelId(tlChannel.getId());
            channelRequest.setAccessHash(tlChannel.getAccessHash());
            request.setChannel(channelRequest);
            request.setFilter(new TLChannelParticipantsFilterAdmins());

            participants = kernel.getKernelComm().doRpcCallSync(request);
            if (participants != null && participants.getParticipants() != null && participants.getUsers() != null) {
                List<User> users = participants.getUsers().stream().map(TelegramConverter::convertUser).collect(Collectors.toList());

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i) != null) {
                        User newUser = users.get(i);
                        User oldUser = userRepository.find(users.get(i).getId());
                        if (oldUser != null) {
                            newUser.getAdmins().addAll(oldUser.getAdmins());
                        }
                        TLAbsChannelParticipant participant = participants.getParticipants().get(i);
                        if (participant instanceof TLChannelParticipantCreator || participant instanceof TLChannelParticipantModerator) {
                            newUser.getAdmins().add(String.valueOf(tlChannel.getId()));
                        }
                        userRepository.save(newUser);
                    }
                }
            }

            currentOffset++;
        } while (participants != null && participants.getParticipants() != null && participants.getParticipants().size() > 0);
    }

    private void saveChat(TLAbsChat tlChat) {
        try {
            if (tlChat instanceof TLChannel) {
                TLChannel tlChannel = (TLChannel) tlChat;
                log.info("Saving " + tlChannel.getTitle() + "#" + tlChannel.getId());

                channelRepository.save(TelegramConverter.convertChannel(tlChannel));
                User user = new User();
                user.setUserName(tlChannel.getTitle());
                user.setId(String.valueOf(tlChannel.getId()));
                user.setHash(String.valueOf(tlChannel.getAccessHash()));
                user.getAdmins().add(user.getId());
                userRepository.save(user);
                if ((tlChannel.getFlags() & 256)!= 0) {
                    saveChannelParticipants(tlChannel, new TLChannelParticipantsFilterAdmins());
                }
            } else {
                if (tlChat instanceof TLChat) {
                    log.debug("Ignore chat " + ((TLChat) tlChat).getTitle() + "#" + tlChat.getId());
                } else {
                    log.debug("Ignore non-channel #" + tlChat.getId());
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException("Chat " + ((TLChannel) tlChat).getTitle() + " loading is aborted", e);
        } catch (RpcException e) {
            throw new RuntimeException("Error while " + ((TLChannel) tlChat).getTitle() + " info loading", e);
        }
    }

    private void loadChannelsInformation() {
        try {
            TLRequestMessagesGetAllChats allChatsRequest = new TLRequestMessagesGetAllChats();
            allChatsRequest.setExceptIds(new TLIntVector());
            kernel.getKernelComm().doRpcCallSync(allChatsRequest).getChats().forEach(this::saveChat);
        } catch (ExecutionException e) {
            throw new RuntimeException("Info loading is aborted", e);
        } catch (RpcException e) {
            throw new RuntimeException("Error while info loading", e);
        }
    }

    private void indexHistory(Channel channel, Instant startDate) {
        try {
            final int batchSize = 100; //Max batch size available from Telegram

            Instant now = Instant.now();
            int offset = 0;

            log.info("Loading history for channel " + channel);
            TLInputPeerChannel tlPeerChannel = new TLInputPeerChannel();
            tlPeerChannel.setChannelId(Integer.valueOf(channel.getId()));
            tlPeerChannel.setAccessHash(Long.valueOf(channel.getHash()));

            TLMessage lastMessage;
            do {
                lastMessage = null;
                TLRequestMessagesGetHistory historyRequest = new TLRequestMessagesGetHistory();
                historyRequest.setPeer(tlPeerChannel);

                historyRequest.setLimit(batchSize);
                historyRequest.setAddOffset(offset);
                historyRequest.setOffsetDate((int)startDate.getEpochSecond());

                TLAbsMessages historyResponse = kernel.getKernelComm().doRpcCallSync(historyRequest);

                if (historyResponse != null && historyResponse.getMessages() != null) {
                    for (TLAbsMessage tlAbsMessage : historyResponse.getMessages()) {
                        if (tlAbsMessage instanceof TLMessage) {
                            lastMessage = (TLMessage) tlAbsMessage;
                            indexMessage(lastMessage);
                        } else {
                            System.err.println("Unknown message type " + tlAbsMessage);
                        }
                    }
                }

                if (historyResponse == null) { //FLOOD WAIT 20
                    Thread.sleep(30 * 1000);
                } else {
                    offset += batchSize;
                }
            } while (lastMessage != null && lastMessage.getDate() < now.getEpochSecond());

        } catch (ExecutionException | RpcException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractMessageContent(TLMessage tlMessage) {
        String message = null;
        if (StringUtils.notEmpty(tlMessage.getMessage())) {
            message = tlMessage.getMessage();
        } else if (tlMessage.getMedia() != null) {
            try {
                Method method = tlMessage.getMedia().getClass().getMethod("getCaption");
                message = String.valueOf(method.invoke(tlMessage.getMedia()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                //It's OK
            }
        }

        if (message != null) {
            return message;
        } else {
            System.err.println("Can not find message content for " + tlMessage);
            return "";
        }
    }

    private void indexMessage(TLMessage tlMessage) {
        String messageContent = extractMessageContent(tlMessage);
        if (StringUtils.notEmpty(messageContent)) {
            Message message = new Message();
            message.setId(String.valueOf(tlMessage.getId()));
            Channel channel = channelRepository.find(String.valueOf(tlMessage.getChatId()));

            if (channel != null) {
                message.setChannel(channel.getName());
            } else {
                if (String.valueOf(tlMessage.getChatId()).equals(SELF)) {
                    message.setChannel("SELF");
                } else {
                    log.warn("Can not find channel #" + tlMessage.getChatId());
                }
            }

            int fromId = tlMessage.getFromId() != 0 ? tlMessage.getFromId() : tlMessage.getChatId();
            User user = userRepository.find(String.valueOf(fromId));
            if (user != null) {
                message.setAuthor(user.getUserName());
            } else {
                log.warn("Can not find user #" + fromId);
            }

            message.setBody(messageContent);
            message.setTimestamp(Instant.ofEpochSecond(tlMessage.getDate()));
            messageService.index(message);
        }
    }

    private void checkJoinMessage(TLMessage tlMessage) {
        log.info("Incoming message:\n" + tlMessage.getMessage());
        Channel channel = channelRepository.find(String.valueOf(tlMessage.getChatId()));
        User user = userRepository.find(String.valueOf(tlMessage.getFromId()));
        if (channel != null && user != null) {
            TLRequestChannelsGetParticipant participantRequest = new TLRequestChannelsGetParticipant();

            TLInputChannel channelRequest = new TLInputChannel();
            channelRequest.setChannelId(Integer.valueOf(channel.getId()));
            channelRequest.setAccessHash(Long.valueOf(channel.getHash()));
            participantRequest.setChannel(channelRequest);

            TLInputUser userRequest = new TLInputUser();
            userRequest.setUserId(Integer.valueOf(user.getId()));
            userRequest.setAccessHash(Long.valueOf(user.getHash()));
            participantRequest.setUserId(userRequest);
            try {
                TLChannelParticipant response = kernel.getKernelComm().getApi().doRpcCall(participantRequest);
                if (response.getParticipant() instanceof TLChannelParticipantCreator || response.getParticipant() instanceof TLChannelParticipantModerator) {
                    startIndexing(pendingChannels.getById(String.valueOf(tlMessage.getChatId())));
                }
            } catch (TimeoutException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Can not find user or id for message: " + tlMessage);
        }
    }

    public void startIndexing(JoinInfo info) {
        pendingChannels.delete(info);
        joinedChannels.add(info.getChannel().getId());
        indexHistory(info.getChannel(), info.getIndexingStart());
    }

    private void authorize() {
        LoginStatus status = null;
        try {
            status = kernel.init();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Failed to init telegram core", e);
        }
        if (status == LoginStatus.CODESENT) {
            boolean success = kernel.getKernelAuth().setAuthCode(configuration.getAuthCode());
            if (success) {
                status = LoginStatus.ALREADYLOGGED;
            }
        }

        if (status == LoginStatus.ALREADYLOGGED) {
//            loadChannelsInformation();
            kernel.startBot();
        } else {
            throw new RuntimeException("Failed to log in: " + status);
        }
    }

    @Override
    public void startListening() {
        final TelegramDatabase databaseManager = new TelegramDatabase(userRepository, channelRepository);

        final ChatUpdatesBuilderImpl builder = new ChatUpdatesBuilderImpl(CustomUpdatesHandler.class);
        builder
                .setDatabaseManager(databaseManager)
                .setTlMessageHandler(updatesHandler);

        kernel = new TelegramBot(configuration, builder, configuration.getApiKey(), configuration.getApiHash());
        authorize();
    }

    private class UpdatesHandler implements TelegramUpdatesHandler {
        @Override
        public void onMessage(TLMessage tlMessage) {
            if (joinedChannels.contains(String.valueOf(tlMessage.getChatId())) && tlMessage.getFwdFrom() == null) {
                indexMessage(tlMessage);
            } else if (pendingChannels.getByJoinHash(tlMessage.getMessage()) != null) {
                checkJoinMessage(tlMessage);
            } else {
                log.warn("Ignoring message (chat#" + tlMessage.getChatId() + "):\n" + tlMessage.getMessage());
            }
        }

        @Override
        public void onUpdatedMessage(TLUpdateShortMessage tlMessage) {
            Message message = messageService.getById(String.valueOf(tlMessage.getId()));
            if (message != null) {
                System.err.println("Updating message");
                message.setBody(tlMessage.getMessage());
                messageService.updateMessage(message);
            }
        }
    }

    private class ApiHandler implements TelegramApiHandler {
        private TLChannel join(String channel) throws ChannelNotFoundException, ChannelAlreadyJoinedException {
            try {
                if (channel.contains("telegram.me/joinchat") || channel.contains("t.me/joinchat")) {
                    String hash = channel.substring(channel.lastIndexOf('/') + 1);
                    TLRequestMessagesImportChatInvite in = new TLRequestMessagesImportChatInvite();
                    in.setHash(hash);
                    TLUpdates bb = (TLUpdates) kernel.getKernelComm().getApi().doRpcCall(in);
                    return (TLChannel) bb.getChats().get(0);
                } else {
                    String channelName;
                    if (channel.contains("im?p=@")) {
                        channelName = channel.substring(channel.lastIndexOf('@') + 1);
                    } else if (channel.contains("telegram.me/") || channel.contains("t.me/")) {
                        channelName = channel.substring(channel.lastIndexOf('/') + 1);
                    } else {
                        channelName = channel;
                    }
                    TLRequestContactsResolveUsername ru = new TLRequestContactsResolveUsername();
                    ru.setUsername(channelName);
                    TLResolvedPeer peer = kernel.getKernelComm().getApi().doRpcCall(ru);

                    if (peer.getChats().size() > 0) {
                        TLChannel tlChannel = (TLChannel) peer.getChats().get(0);

                        TLRequestChannelsJoinChannel join = new TLRequestChannelsJoinChannel();
                        TLInputChannel ch = new TLInputChannel();
                        ch.setChannelId(tlChannel.getId());
                        ch.setAccessHash(tlChannel.getAccessHash());
                        join.setChannel(ch);

                        TLAbsUpdates bb = kernel.getKernelComm().getApi().doRpcCall(join);
                        return tlChannel;
                    } else {
                        //No channels found
                        return null;
                    }


                }
            } catch (RpcException e) {
                Set<String> errorsForNotFound = new HashSet<String>() {{
                    add("INVITE_HASH_INVALID");
                    add("USERNAME_INVALID");
                    add("USERNAME_NOT_OCCUPIED");
                }};

                if ("USER_ALREADY_PARTICIPANT".equals(e.getErrorTag())) {
                    throw new ChannelAlreadyJoinedException();
                } else if (errorsForNotFound.contains(e.getErrorTag())) {
                    throw new ChannelNotFoundException();
                } else {
                    throw new RuntimeException("Unknown error occurred", e);
                }
            } catch (TimeoutException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public JoinInfo joinChannel(String channel) throws ChannelNotFoundException, ChannelAlreadyJoinedException {
            return joinChannel(channel, Instant.now());
        }

        @Override
        public JoinInfo joinChannel(String channelLink, Instant indexingStart) throws ChannelNotFoundException, ChannelAlreadyJoinedException {
            TLChannel joinedChannel = join(channelLink);
            saveChat(joinedChannel);
            JoinInfo joinInfo = new JoinInfo();
            joinInfo.setChannel(TelegramConverter.convertChannel(joinedChannel));
            joinInfo.setIndexingStart(indexingStart);
            joinInfo.setJoinHash(UUID.randomUUID().toString());

            pendingChannels.add(joinInfo);

            return joinInfo;
        }
    }

}
