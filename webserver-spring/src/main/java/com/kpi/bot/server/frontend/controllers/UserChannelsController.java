package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.exceptions.ChannelNotFoundException;
import com.kpi.bot.server.frontend.data.JoinChannelRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.exceptions.ChannelAlreadyJoinedException;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
public class UserChannelsController {
    private TelegramClient telegramClient;
    private Repository<Channel> channelRepository;

    @Autowired
    public UserChannelsController(Repository<Channel> channelRepository, TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
        this.channelRepository = channelRepository;
    }

    @PutMapping
    public Object joinChannel(@RequestBody JoinChannelRequest request) {
        try {
            JoinInfo info = telegramClient.getApiHandler().joinChannel(request.getChannel());
            return ResponseBuilder.OK().add("code", info.getJoinHash()).build();
        } catch (ChannelNotFoundException | ChannelAlreadyJoinedException e) {
            return ResponseBuilder.ERROR(e.getMessage()).build();
        }
    }

    @GetMapping
    public Object getChannels() {
        return ResponseBuilder.OK().add("channels", telegramClient.getJoinedChannels()).build();
    }


}
