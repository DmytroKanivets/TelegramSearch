package com.kpi.bot.services;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Statistics {

    @Data
    @AllArgsConstructor
    public static class StatisticsEntry {
        private String name;
        private long time;
        private long messages;
    }


    public static final String CHANNELS_ALL = "all";
    public static final String CHANNELS_PREFIX = "channel_";
    public static final String SEARCH = "search";

    private static final Map<String, Long> time = new ConcurrentHashMap<>();
    private static final Map<String, Long> count = new ConcurrentHashMap<>();

    public static void addTime(String category, long time) {
        addTime(category, time, 1);
    }

    public static void addTime(String category, long time, long count) {
        Statistics.time.computeIfAbsent(category, (s -> 0L));
        Statistics.time.compute(category, (s, value) -> value + time);
        Statistics.count.computeIfAbsent(category, (s -> 0L));
        Statistics.count.compute(category, (s, value) -> value + count);
    }

    public static long getCount(String category) {
        return count.getOrDefault(category, 0L);
    }

    public static long getTime(String category) {
        return time.getOrDefault(category, 0L);
    }

    public static double getAverage(String category) {
        return time.getOrDefault(category, 0L) / time.getOrDefault(category, 1L);
    }

    public static List<String> getKeys() {
        return new LinkedList<>(time.keySet());
    }

    public static void remove(String s) {
        time.remove(s);
        count.remove(s);
    }

    public static Statistics.StatisticsEntry getEntry(String name) {
        return new StatisticsEntry(name, getTime(name), getCount(name));
    }
}
