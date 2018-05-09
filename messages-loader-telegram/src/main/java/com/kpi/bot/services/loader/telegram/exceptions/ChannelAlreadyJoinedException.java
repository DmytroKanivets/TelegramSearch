package com.kpi.bot.services.loader.telegram.exceptions;

public class ChannelAlreadyJoinedException extends Exception {
    public ChannelAlreadyJoinedException() {
        super("Channel is alredy joined");
    }
}
