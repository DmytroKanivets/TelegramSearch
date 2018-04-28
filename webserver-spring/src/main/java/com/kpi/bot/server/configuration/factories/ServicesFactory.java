package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.impl.MessageServiceImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServicesFactory {

    @Bean
    @Scope("prototype")
    @Autowired
    public MessageService createMessageService(SearchableRepository<Message> messageRepository) {
        return new MessageServiceImpl(messageRepository);
    }
}
