package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.data.Repository;
import com.kpi.bot.database.lucene.StorageStrategy;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.ChannelsService;
import com.kpi.bot.services.Statistics;
import com.kpi.bot.stats.IndexingStatistics;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {
    private final ChannelsService channelsService;
    private final Repository<Message> messageRepository;

    @Autowired
    public AdminStatsController(ChannelsService channelsService, Repository<Message> messageRepository) {
        this.channelsService = channelsService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/messages")
    public Object getMessagesStats() {
        Set<String> channels = channelsService.getChannels().stream().map(Channel::getName).collect(Collectors.toSet());
        IndexingStatistics statistics = IndexingStatistics.getIndexingStatistics();
        for (Statistics.StatisticsEntry is : statistics.getStatistics()) {
            channels.remove(is.getName());
        }

        for (String channel : channels) {
            statistics.getStatistics().add(new Statistics.StatisticsEntry(channel, 0, 0));
        }
        return ResponseBuilder.OK().add("stats", statistics).build();
    }

    private long getIndexSize() {
        switch (StorageStrategy.getStorageStrategy()) {
            case FILE:
                return new File(StorageStrategy.INDEX_NAME).length();
            case MEMORY:
                return ((RAMDirectory)StorageStrategy.MEMORY.getDirectory()).ramBytesUsed();
            default:
                throw new RuntimeException("Can not identify StorageStrategy");
        }
    }

    private long getMessageMemory() {
        return ObjectSizeCalculator.getObjectSize(messageRepository);
    }

    private long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @GetMapping("/memory")
    public Object getMemoryStats() {
        ResponseBuilder responseBuilder = ResponseBuilder.OK();

        responseBuilder.add("indexMemory", getIndexSize());
        responseBuilder.add("messageMemory", getMessageMemory());
        responseBuilder.add("totalMemory", getTotalMemory());

        return responseBuilder.build();
    }

    private int getTotalMessage() {
        return messageRepository.getSize();
    }

    private Statistics.StatisticsEntry getMessagesInfo() {
        return Statistics.getEntry(Statistics.CHANNELS_PREFIX + Statistics.CHANNELS_ALL);
    }

    private Statistics.StatisticsEntry getSearchInfo() {
        return Statistics.getEntry(Statistics.SEARCH);
    }

    @GetMapping("/indexing")
    public Object getIndexingStats() {
        ResponseBuilder responseBuilder = ResponseBuilder.OK();

        responseBuilder.add("totalMessages", getTotalMessage());
        responseBuilder.add("messagesInfo", getMessagesInfo());
        responseBuilder.add("searchInfo", getSearchInfo());


        return responseBuilder.build();
    }

}
