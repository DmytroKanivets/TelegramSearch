package com.kpi.bot.services.loader.telegram.structure;

import org.telegram.bot.structure.Chat;

public class TelegramChat implements Chat {
    private int id;
    private Long accessHash;
    private boolean isChannel;

    public TelegramChat(int id) {
        this.id = id;
    }

    public TelegramChat() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Long getAccessHash() {
        return accessHash;
    }

    @Override
    public boolean isChannel() {
        return isChannel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccessHash(Long accessHash) {
        this.accessHash = accessHash;
    }

    public void setChannel(boolean channel) {
        isChannel = channel;
    }
}
