package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.database.inmemory.InMemoryDatabase;
import com.kpi.bot.entity.data.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DatabaseFactory {
    @Bean
    @Scope("prototype")
    public Repository<Message> createMessageRepository() {
        return new InMemoryDatabase();
    }
}
