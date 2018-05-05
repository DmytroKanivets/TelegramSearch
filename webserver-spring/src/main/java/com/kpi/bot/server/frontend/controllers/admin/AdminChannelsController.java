package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.exceptions.ChannelNotFoundException;
import com.kpi.bot.server.frontend.data.JoinChannelRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.ChannelsService;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.loader.telegram.ChannelJoinException;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.exceptions.ChannelAlreadyJoinedException;
import com.kpi.bot.services.loader.telegram.structure.JoinInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/channels")
public class AdminChannelsController {
    private TelegramClient telegramClient;

    private ChannelsService channelsService;

    @Autowired
    public AdminChannelsController(TelegramClient telegramClient, ChannelsService channelsService) {
        this.channelsService = channelsService;
        this.telegramClient = telegramClient;
    }

    @DeleteMapping("/name/{name}")
    public Object deleteChannelByName(@PathVariable String name) {
        try {
            channelsService.deleteChannelByName(name);
        } catch (ChannelNotFoundException e) {
            return ResponseBuilder.ERROR(e.getMessage()).build();
        }
        return ResponseBuilder.OK().build();
    }


    @DeleteMapping("/id/{id}")
    public Object deleteChannelById(@PathVariable String id) {
        try {
            channelsService.deleteChannelById(id);
        } catch (Exception e) {
            return ResponseBuilder.ERROR(e.getMessage()).build();
        }
        return ResponseBuilder.OK().build();
    }

    @PutMapping
    public Object joinChannel(@RequestBody JoinChannelRequest request) {
        try {
            JoinInfo info = telegramClient.getApiHandler().joinChannel(request.getChannel());
            telegramClient.startIndexing(info);
            return ResponseBuilder.OK().add("channel", info.getChannel().getName()).build();
        } catch (ChannelNotFoundException | ChannelAlreadyJoinedException e) {
            return ResponseBuilder.ERROR(e.getMessage()).build();
        }
    }
}