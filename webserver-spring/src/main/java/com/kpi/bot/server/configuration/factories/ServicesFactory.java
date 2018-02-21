package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.impl.MessageServiceImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServicesFactory {

    private DatabaseFactory databaseFactory;

    @Autowired
    public ServicesFactory(DatabaseFactory factory) {
        databaseFactory = factory;
    }

    @Bean
    @Scope("prototype")
    public MessageService createMessageService() {
        return new MessageServiceImpl(databaseFactory.createMessageRepository());
    }
}
