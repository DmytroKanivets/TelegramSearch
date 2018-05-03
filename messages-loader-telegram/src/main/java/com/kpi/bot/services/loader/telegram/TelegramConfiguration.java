package com.kpi.bot.services.loader.telegram;

import org.telegram.bot.structure.BotConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramConfiguration extends BotConfig {

    public static TelegramConfiguration defaultConfiguration() {
        Integer apiKey = null;
        try {
            apiKey = Integer.valueOf(System.getProperty("telegram.api.key"));
        } catch (NumberFormatException e) {
            //We will throw exception later
            throw new RuntimeException(e);
        }
        String apiHash = System.getProperty("telegram.api.hash");
        String apiPhone = System.getProperty("telegram.api.phone");

        if (apiKey == null || apiHash == null || apiPhone == null) {
            throw new RuntimeException("Default telegram configuration is not specified");
        } else {
            return new TelegramConfiguration(151369, "12f93c908c66d32bdf975a483b3f0691", "+380688109527");
        }
    }

    private int apiKey;
    private String apiHash;
    private String phoneNumber;

    private String code;
    private volatile boolean codePresent = false;

    private Set<Integer> channels = ConcurrentHashMap.newKeySet();

    public TelegramConfiguration(int apiKey, String apiHash, String phoneNumber) {
        this.apiKey = apiKey;
        this.apiHash = apiHash;
        this.phoneNumber = phoneNumber;

        channels = new HashSet<>();
    }

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getApiKey() {
        return apiKey;
    }

    public String getApiHash() {
        return apiHash;
    }

    public boolean addChannel(int channelId) {
        return channels.add(channelId);
    }

    public boolean removeChannel(int channelId) {
        return channels.remove(channelId);
    }

    public boolean indexChannel(int channelId) {
        return channels.contains(channelId);
    }

    public void setAuthCode(String code) {
        this.code = code;
        codePresent = true;
    }

    public String getAuthCode() {
        while (!codePresent) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return code;
    }
}
