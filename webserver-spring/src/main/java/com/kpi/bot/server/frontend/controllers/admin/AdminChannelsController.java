package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.server.frontend.data.JoinChannelRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.loader.telegram.ChannelJoinException;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/channels")
public class AdminChannelsController {
    private TelegramClient telegramClient;

    @Autowired
    public AdminChannelsController(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @DeleteMapping("/{id}")
    public Object deleteChannel(@PathVariable String id) {
        telegramClient.stopIndexing(id);
        return ResponseBuilder.OK().build();
    }

    @PutMapping
    public Object joinChannel(@RequestBody JoinChannelRequest request) {
        try {
            JoinInfo info = telegramClient.getApiHandler().joinChannel(request.getChannel());
            telegramClient.startIndexing(info);
            return ResponseBuilder.OK().add("channel", info.getChannel().getName()).build();
        } catch (ChannelJoinException e) {
            return ResponseBuilder.ERROR().add("message", e.getMessage()).build();
        }
    }
}