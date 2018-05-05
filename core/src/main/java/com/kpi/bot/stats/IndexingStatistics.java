package com.kpi.bot.stats;

import com.kpi.bot.services.Statistics;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class IndexingStatistics {
    private Statistics.StatisticsEntry total;
    private List<Statistics.StatisticsEntry> statistics;

    public IndexingStatistics() {
        this.total = new Statistics.StatisticsEntry("total", Statistics.getTime(Statistics.CHANNELS_PREFIX + Statistics.CHANNELS_ALL), Statistics.getCount(Statistics.CHANNELS_PREFIX + Statistics.CHANNELS_ALL));
        this.statistics = new LinkedList<>();

        for (String entry : Statistics.getKeys()) {
            if (entry.startsWith(Statistics.CHANNELS_PREFIX) && !entry.equals(Statistics.CHANNELS_PREFIX + Statistics.CHANNELS_ALL)) {
                String name = entry.substring(Statistics.CHANNELS_PREFIX.length());
                statistics.add(new Statistics.StatisticsEntry(name, Statistics.getTime(entry), Statistics.getCount(entry)));
            }
        }
    }

    public static void messageIndexed(String channel, long time) {
        Statistics.addTime(Statistics.CHANNELS_PREFIX + channel, time);
        Statistics.addTime(Statistics.CHANNELS_PREFIX + Statistics.CHANNELS_ALL, time);
    }

    public static IndexingStatistics getIndexingStatistics() {
        return new IndexingStatistics();
    }

    public static void removeChannel(String channel) {
        Statistics.remove(Statistics.CHANNELS_PREFIX + channel);
    }

}
