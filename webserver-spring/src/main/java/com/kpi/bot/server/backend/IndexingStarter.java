package com.kpi.bot.server.backend;

import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.database.lucene.LuceneDatabase;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.loader.telegram.TelegramClient;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Component("singleton")
public class IndexingStarter {

    @Autowired
    SearchableRepository<Message> database;

//    @PostConstruct
    private void fillTestData() {
        database.saveAll(Arrays.asList(
                Message.builder().id("1").author("me").body("hey hey").channel("channel 1").timestamp(Instant.now().minus(Duration.ofDays(1))).build(),
                Message.builder().id("2").author("me").body("hey la").channel("channel 2").timestamp(Instant.now().minus(Duration.ofDays(2))).build(),
                Message.builder().id("3").author("him").body("hey lala").channel("channel 3").timestamp(Instant.now().minus(Duration.ofDays(3))).build(),
                Message.builder().id("4").author("him").body("lala lalala").channel("channel 4").timestamp(Instant.now().minus(Duration.ofDays(4))).build(),
                Message.builder().id("5").author("him").body("lala qweqweqw").channel("channel 4").timestamp(Instant.now().minus(Duration.ofDays(4))).build(),
                Message.builder().id("6").author("channel 4").body("lala rewtwewef").channel("channel 4").timestamp(Instant.now().minus(Duration.ofDays(4))).build(),
                Message.builder().id("7").author("channel 5").body("lala lrewr weral rwerala").channel("channel 6").timestamp(Instant.now().minus(Duration.ofDays(4))).build()
        ));
    }

    @Autowired
    public IndexingStarter(TelegramClient telegramClient) {
//        fillTestData();

//        BotLogger.setLevel(Level.OFF);
        new Thread(telegramClient::startListening).start();
    }


}