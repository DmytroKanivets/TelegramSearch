package com.kpi.bot.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Statistics {
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
}
