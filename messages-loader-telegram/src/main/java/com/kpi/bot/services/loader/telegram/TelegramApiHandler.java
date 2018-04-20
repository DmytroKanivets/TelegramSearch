package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.services.loader.telegram.structure.JoinInfo;

import java.time.Instant;

public interface TelegramApiHandler {
    JoinInfo joinChannel(String channel) throws ChannelJoinException;
    JoinInfo joinChannel(String channel, Instant indexingStart) throws ChannelJoinException;
}
