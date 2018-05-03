package com.kpi.bot.exceptions;

public class ChannelNotFoundException extends Exception {
    public ChannelNotFoundException() {
        super("Channel not found");
    }
}
