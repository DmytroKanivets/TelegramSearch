package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.exceptions.ChannelNotFoundException;
import com.kpi.bot.services.loader.telegram.exceptions.ChannelAlreadyJoinedException;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;

import java.time.Instant;

public interface TelegramApiHandler {
    JoinInfo joinChannel(String channel) throws ChannelNotFoundException, ChannelAlreadyJoinedException;
    JoinInfo joinChannel(String channel, Instant indexingStart) throws ChannelNotFoundException, ChannelAlreadyJoinedException;
}
