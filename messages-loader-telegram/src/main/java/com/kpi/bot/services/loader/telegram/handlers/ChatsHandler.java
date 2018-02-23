package com.kpi.bot.services.loader.telegram.handlers;

import com.kpi.bot.services.loader.telegram.structure.TelegramChat;
import org.telegram.api.chat.TLAbsChat;
import org.telegram.api.chat.TLChat;
import org.telegram.api.chat.TLChatForbidden;
import org.telegram.api.chat.channel.TLChannel;
import org.telegram.api.chat.channel.TLChannelForbidden;
import org.telegram.bot.handlers.interfaces.IChatsHandler;
import com.kpi.bot.services.loader.telegram.database.TelegramDatabase;

import java.util.List;

public class ChatsHandler implements IChatsHandler {
    private static final String LOGTAG = "CHATSHANDLER";
    private final TelegramDatabase databaseManager;

    public ChatsHandler(TelegramDatabase databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void onChats(List<TLAbsChat> chats) {
        chats.forEach(this::onAbsChat);
    }

    private void onAbsChat(TLAbsChat chat) {
        if (chat instanceof TLChannel) {
            onChannel((TLChannel) chat);
        } else if (chat instanceof TLChannelForbidden) {
            onChannelForbidden((TLChannelForbidden) chat);
        } else if (chat instanceof TLChat) {
            onChat((TLChat) chat);
        } else if (chat instanceof TLChatForbidden) {
            onChatForbidden((TLChatForbidden) chat);
        } else {
             throw new RuntimeException("Unsupported chat type " + chat);
        }
    }

    private void onChatForbidden(TLChatForbidden chat) {
        onChat(chat.getId());
    }

    private void onChat(TLChat chat) {
        onChat(chat.getId());
    }

    private void onChat(int chatId) {
        boolean updating = true;
        TelegramChat current = (TelegramChat) databaseManager.getChatById(chatId);
        if (current == null) {
            updating = false;
            current = new TelegramChat(chatId);
        }
        current.setChannel(false);

        if (updating) {
            databaseManager.updateChat(current);
        } else {
            databaseManager.addChat(current);
        }
    }


    private void onChannelForbidden(TLChannelForbidden channel) {
        boolean updating = true;
        TelegramChat current = (TelegramChat) databaseManager.getChatById(channel.getId());
        if (current == null) {
            updating = false;
            current = new TelegramChat(channel.getId());
        }
        current.setChannel(true);
        current.setAccessHash(channel.getAccessHash());

        if (updating) {
            databaseManager.updateChat(current);
        } else {
            databaseManager.addChat(current);
        }
    }

    private void onChannel(TLChannel channel) {
        boolean updating = true;
        TelegramChat current = (TelegramChat) databaseManager.getChatById(channel.getId());
        if (current == null) {
            updating = false;
            current = new TelegramChat(channel.getId());
        }
        current.setChannel(true);
        if (channel.hasAccessHash()) {
            current.setAccessHash(channel.getAccessHash());
        }

        if (updating) {
            databaseManager.updateChat(current);
        } else {
            databaseManager.addChat(current);
        }
    }

}
