package com.kpi.bot.services.loader.telegram;

import org.telegram.api.engine.RpcException;

public class ChannelJoinException extends Exception {
    private int errorCode;

    public ChannelJoinException(String message) {
        super(message);
    }

    public ChannelJoinException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelJoinException(RpcException e) {
        super(e.getMessage(), e);
        this.errorCode = e.getErrorCode();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
