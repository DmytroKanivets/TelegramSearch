package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.database.inmemory.Database;
import com.kpi.bot.database.lucene.LuceneDatabase;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.data.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

@Configuration
public class DatabaseFactory {
    @Bean
    @Scope("singleton")
    public SearchableRepository<Message> createSearchableMessageRepository(Repository<Message> messageRepository) {
//        return new MessagesDatabase();
        return new LuceneDatabase<>(messageRepository, Arrays.asList("channel", "author", "timestamp"), Arrays.asList("body"));
    }


    @Bean
    @Primary
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
