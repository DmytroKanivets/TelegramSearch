package com.kpi.bot.services.loader.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.bot.structure.BotConfig;

@Data
@AllArgsConstructor
public class TelegramConfiguration extends BotConfig {
    private int apiKey;
    private String apiHash;
    private String phoneNumber;

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public boolean isBot() {
        return false;
    }
}
