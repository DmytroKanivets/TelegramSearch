package com.kpi.bot.utils;

import com.kpi.bot.services.Statistics;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class IndexingStatistics {

    @Data
    @AllArgsConstructor
    public static class ChannelStatistics {
        private String name;
        private long time;
        private long messages;
    }

    private ChannelStatistics total;
    private List<ChannelStatistics> statistics;

    public IndexingStatistics() {
        this.total = new ChannelStatistics("total", Statistics.getTime(CHANNELS_PREFIX + CHANNELS_ALL), Statistics.getCount(CHANNELS_PREFIX + CHANNELS_ALL));
        this.statistics = new LinkedList<>();

        for (String entry : Statistics.getKeys()) {
            if (entry.startsWith(CHANNELS_PREFIX)) {
                String name = entry.substring(CHANNELS_PREFIX.length());
                statistics.add(new ChannelStatistics(name, Statistics.getTime(entry), Statistics.getCount(entry)));
            }
        }
    }

    private static final String CHANNELS_ALL = "all";
    private static final String CHANNELS_PREFIX = "channel_";

    public static void messageIndexed(String channel, long time) {
        Statistics.addTime(CHANNELS_PREFIX + channel, time);
        Statistics.addTime(CHANNELS_PREFIX + CHANNELS_ALL, time);
    }

    public static IndexingStatistics getIndexingStatistics() {
        return new IndexingStatistics();
    }
}
