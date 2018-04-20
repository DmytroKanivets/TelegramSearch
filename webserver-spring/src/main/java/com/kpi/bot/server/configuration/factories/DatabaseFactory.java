package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.database.inmemory.Database;
import com.kpi.bot.database.inmemory.MessagesDatabase;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.loader.telegram.database.TelegramRepository;
import com.kpi.bot.services.loader.telegram.structure.Channel;
import com.kpi.bot.services.loader.telegram.structure.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DatabaseFactory {
    @Bean
    @Scope("singleton")
    public SearchableRepository<Message> createMessageRepository() {
        return new MessagesDatabase();
    }

    @Bean
    @Scope("singleton")
    public Repository<User> createUserRepository() {
        return new Database<User>();
    }

    @Bean
    @Scope("singleton")
    public Repository<Channel> createChannelRepository() {
        return new Database<Channel>();
    }
}
