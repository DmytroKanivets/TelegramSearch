package com.kpi.bot.server.configuration.factories;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.User;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import com.kpi.bot.services.loader.telegram.TelegramConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
//@PropertySource("classpath:/telegram.properties")
public class TelegramClientFactory {

    private DatabaseFactory factory;
    @Value("${telegram.api.key}")
    private Integer apiKey;
    @Value("${telegram.api.hash}")
    private String apiHash;
    @Value("${telegram.api.phone}")
    private String phone;

    @Bean
    @Scope("singleton")
    public TelegramConfiguration createTelegramConfiguration() {
        if (apiKey == null || apiHash == null || phone == null) {
            throw new RuntimeException("Default telegram configuration is not specified");
        } else {
            return new TelegramConfiguration(apiKey, apiHash, phone);
        }
    }

    @Bean
    @Scope("singleton")
    public TelegramClient getTelegramClient(TelegramConfiguration configuration, MessageService messageService, Repository<User> userRepository, Repository<Channel> channelRepository) {
        return new TelegramClient(configuration, messageService, userRepository, channelRepository);

    }

}
