package com.kpi.bot.server.backend;

import com.kpi.bot.services.loader.telegram.TelegramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("singleton")
public class IndexingStarter {

    @Autowired
    public IndexingStarter(TelegramClient telegramClient) {
//        BotLogger.setLevel(Level.OFF);
        new Thread(telegramClient::startListening).start();
    }


}