package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.ChannelsService;
import com.kpi.bot.services.Statistics;
import com.kpi.bot.stats.IndexingStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {
    private final ChannelsService channelsService;

    @Autowired
    public AdminStatsController(ChannelsService channelsService) {
        this.channelsService = channelsService;
    }

    @GetMapping("/messages")
    public Object getMessagesStats() {
        Set<String> channels = channelsService.getChannels().stream().map(Channel::getName).collect(Collectors.toSet());
        IndexingStatistics statistics = IndexingStatistics.getIndexingStatistics();
        for (IndexingStatistics.ChannelStatistics is : statistics.getStatistics()) {
            channels.remove(is.getName());
        }

        for (String channel : channels) {
            statistics.getStatistics().add(new IndexingStatistics.ChannelStatistics(channel, 0, 0));
        }
        return ResponseBuilder.OK().add("stats", statistics).build();
    }
}
