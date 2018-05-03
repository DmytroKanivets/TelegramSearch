package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.data.Repository;
import com.kpi.bot.server.frontend.data.JoinChannelRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.loader.telegram.ChannelJoinException;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.structure.Channel;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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
        } catch (ChannelJoinException e) {
            return ResponseBuilder.ERROR().add("message", e.getMessage()).build();
        }
    }

    @GetMapping
    public Object getChannels() {
        return ResponseBuilder.OK().add("channels", channelRepository.findAll().stream().map(Channel::getName).collect(Collectors.toList())).build();
    }


}
