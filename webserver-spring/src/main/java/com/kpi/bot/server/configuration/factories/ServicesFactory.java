package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.ChannelsService;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.impl.ChannelsServiceImpl;
import com.kpi.bot.services.impl.MessageServiceImpl;
import com.kpi.bot.services.loader.MessageLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServicesFactory {

    @Bean
    @Scope("prototype")
    public MessageService createMessageService(SearchableRepository<Message> messageRepository) {
        return new MessageServiceImpl(messageRepository);
    }


    @Bean
    @Scope("prototype")
    public ChannelsService createChannelsService(Repository<Channel> channelRepository, SearchableRepository<Message> messageRepository, MessageLoader messageLoader) {
        return new ChannelsServiceImpl(channelRepository, messageRepository, messageLoader);
    }
}
