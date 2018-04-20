package com.kpi.bot.services.loader.telegram.core;

import com.kpi.bot.services.loader.telegram.TelegramUpdatesHandler;
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

import java.util.List;

public class CustomUpdatesHandler extends DefaultUpdatesHandler {
    private IUsersHandler usersHandler;
    private IChatsHandler chatsHandler;
    private TelegramUpdatesHandler updatesHandler;

    public CustomUpdatesHandler(IKernelComm kernelComm, IDifferenceParametersService differenceParametersService, DatabaseManager databaseManager) {
        super(kernelComm, differenceParametersService, databaseManager);
    }

    public void setHandlers(IUsersHandler usersHandler, IChatsHandler chatsHandler, TelegramUpdatesHandler updatesHandler) {
        this.chatsHandler = chatsHandler;
        this.usersHandler = usersHandler;
        this.updatesHandler = updatesHandler;
    }

    @Override
    public void onTLUpdateShortMessageCustom(TLUpdateShortMessage update) {
        updatesHandler.onUpdatedMessage(update);
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
            onTLMessage((TLMessage) message);
        } else {
            throw new RuntimeException("Unsupported message type: " + message.toString());
        }
    }

    public void onTLMessages(List<TLAbsMessage> messages) {
        messages.forEach(this::onTLAbsMessageCustom);
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
        updatesHandler.onMessage(message);
    }
}
