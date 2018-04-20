package com.kpi.bot.server.backend;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.bot.services.BotLogger;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;

@Component("singleton")
public class IndexingStarter {
    /*
    @Autowired
    public IndexingStarter(TelegramClient telegramClient) {
//        BotLogger.setLevel(Level.OFF);
        new Thread(telegramClient::startListening).start();
    }
    */

    @Autowired
    public IndexingStarter(Repository<Message> messageRepository) {
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #1", "Author #1", "This is message #1", Instant.now()));
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #1", "Author #2", "This is message #2", Instant.now()));
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #2", "Author #2", "This is message #3", Instant.now()));
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #2", "Author #3", "This is message #4", Instant.now()));
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #2", "Author #3", "This is message #5", Instant.now()));
        messageRepository.save(new Message(UUID.randomUUID().toString(), "Channel #3", "Author #4", "This is message #6", Instant.now()));
    }

}