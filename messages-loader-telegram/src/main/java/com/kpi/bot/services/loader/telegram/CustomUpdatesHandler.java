package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.services.loader.telegram.handlers.MessageHandler;
import com.kpi.bot.services.loader.telegram.handlers.TLMessageHandler;
import org.jetbrains.annotations.NotNull;
import org.telegram.api.chat.TLAbsChat;
import org.telegram.api.message.TLAbsMessage;
import org.telegram.api.message.TLMessage;
import org.telegram.api.update.TLUpdateChannelNewMessage;
import org.telegram.api.update.TLUpdateNewMessage;
import org.telegram.api.updates.TLUpdateShortMessage;
import org.telegram.api.user.TLAbsUser;
import org.telegram.bot.handlers.DefaultUpdatesHandler;
import org.telegram.bot.handlers.interfaces.IChatsHandler;
import org.telegram.bot.handlers.interfaces.IUsersHandler;
import org.telegram.bot.kernel.IKernelComm;
import org.telegram.bot.kernel.database.DatabaseManager;
import org.telegram.bot.kernel.differenceparameters.IDifferenceParametersService;
import org.telegram.bot.services.BotLogger;
import org.telegram.bot.structure.BotConfig;
import org.telegram.bot.structure.IUser;

import java.util.List;

public class CustomUpdatesHandler extends DefaultUpdatesHandler {
    private static final String LOGTAG = "CHATUPDATESHANDLER";

    private final DatabaseManager databaseManager;
    private MessageHandler messageHandler;
    private IUsersHandler usersHandler;
    private IChatsHandler chatsHandler;
    private TLMessageHandler tlMessageHandler;

    public CustomUpdatesHandler(IKernelComm kernelComm, IDifferenceParametersService differenceParametersService, DatabaseManager databaseManager) {
        super(kernelComm, differenceParametersService, databaseManager);
        this.databaseManager = databaseManager;
    }

    public void setHandlers(MessageHandler messageHandler, IUsersHandler usersHandler, IChatsHandler chatsHandler, TLMessageHandler tlMessageHandler) {
        this.messageHandler = messageHandler;
        this.chatsHandler = chatsHandler;
        this.usersHandler = usersHandler;
        this.tlMessageHandler = tlMessageHandler;
    }


    @Override
    public void onTLUpdateShortMessageCustom(TLUpdateShortMessage update) {
        final IUser user = databaseManager.getUserById(update.getUserId());
        if (user != null) {
            BotLogger.info(LOGTAG, "Received message from: " + update.getUserId());
            messageHandler.handleMessage(user, update);
        }
    }

    @Override
    public void onTLUpdateNewMessageCustom(TLUpdateNewMessage update) {
        onTLAbsMessageCustom(update.getMessage());
    }

    @Override
    public void onTLUpdateChannelNewMessageCustom(TLUpdateChannelNewMessage update) {
        onTLAbsMessageCustom(update.getMessage());
    }

    @Override
    protected void onTLAbsMessageCustom(TLAbsMessage message) {
        if (message instanceof TLMessage) {
            BotLogger.info(LOGTAG, "Received TLMessage");

            System.out.println("RECEIVE " + ((TLMessage) message).getMessage());
            onTLMessage((TLMessage) message);
        } else {
            BotLogger.info(LOGTAG, "Unsupported TLAbsMessage -> " + message.toString());
        }
    }

    @Override
    protected void onUsersCustom(List<TLAbsUser> users) {
        usersHandler.onUsers(users);
    }

    @Override
    protected void onChatsCustom(List<TLAbsChat> chats) {
        chatsHandler.onChats(chats);
    }

    private void onTLMessage(@NotNull TLMessage message) {
//        if (message.hasFromId()) {
//            final IUser user = databaseManager.getUserById(message.getFromId());
//            if (user != null) {
                this.tlMessageHandler.onTLMessage(message);
//            }
//        }
    }
}
