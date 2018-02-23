package com.kpi.bot.services.loader.telegram.structure;

import org.telegram.bot.structure.IUser;

public class TelegramUser implements IUser {
    private final int userId;
    private Long userHash;

    public TelegramUser(int uid) {
        this.userId = uid;
    }

    public TelegramUser(TelegramUser copy) {
        this.userId = copy.getUserId();
        this.userHash = copy.getUserHash();
    }

    @Override
    public int getUserId() {
        return this.userId;
    }

    @Override
    public Long getUserHash() {
        return userHash;
    }

    public void setUserHash(Long userHash) {
        this.userHash = userHash;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "userId=" + userId +
                ", userHash=" + userHash +
                '}';
    }
}
