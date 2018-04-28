package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.database.inmemory.Database;
import com.kpi.bot.database.inmemory.MessagesDatabase;
import com.kpi.bot.database.lucene.LuceneDatabase;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.loader.telegram.database.TelegramRepository;
import com.kpi.bot.services.loader.telegram.structure.Channel;
import com.kpi.bot.services.loader.telegram.structure.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseFactory {
    @Bean
    @Scope("singleton")
    public SearchableRepository<Message> createSearchableMessageRepository(Repository<Message> messageRepository) {
//        return new MessagesDatabase();
        return new LuceneDatabase<>(messageRepository, Arrays.asList("channel", "author", "timestamp"), Arrays.asList("body"));
    }


    @Bean
    @Scope("singleton")
    public Repository<Message> createMessageRepository() {
        return new Database<>();
    }

    @Bean
    @Scope("singleton")
    public Repository<User> createUserRepository() {
        return new Database<>();
    }

    @Bean
    @Scope("singleton")
    public Repository<Channel> createChannelRepository() {
        return new Database<>();
    }
}
