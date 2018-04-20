package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.server.frontend.data.JoinChannelRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.loader.telegram.ChannelJoinException;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
public class UserChannelsController {
    private TelegramClient telegramClient;

    @Autowired
    public UserChannelsController(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
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


}
