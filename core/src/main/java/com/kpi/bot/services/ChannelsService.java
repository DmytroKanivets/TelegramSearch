package com.kpi.bot.services;

import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.exceptions.ChannelNotFoundException;

import java.util.List;

public interface ChannelsService {
    void save(Channel channel);

    void deleteChannelById(String id);
    void deleteChannelByName(String name) throws ChannelNotFoundException;
    List<Channel> getChannels();
}
