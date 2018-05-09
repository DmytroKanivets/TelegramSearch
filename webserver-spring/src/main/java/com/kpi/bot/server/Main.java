package com.kpi.bot.server;

import com.kpi.bot.data.Repository;
import com.kpi.bot.database.inmemory.Database;
import com.kpi.bot.database.lucene.LuceneDatabase;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    private static void testTokens() throws IOException {
        TokenStream stream = new StandardAnalyzer().tokenStream("body", "hey, lol");
        CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        while (stream.incrementToken()) {
//            System.out.println(charTermAttribute);
        }
        stream.end();
        stream.close();
    }

    public static void main(String[] args) throws IOException {
        Repository<Message> messageRepository = new Database<>();
        LuceneDatabase<Message> database = new LuceneDatabase<>(messageRepository, Arrays.asList("channel", "author", "timestamp"), Arrays.asList("body"));

        database.saveAll(Arrays.asList(
                Message.builder().id("1").author("me").body("hey hey").channel("channel 1").timestamp(Instant.now().minus(Duration.ofDays(1))).build(),
                Message.builder().id("2").author("me").body("hey la").channel("channel 2").timestamp(Instant.now().minus(Duration.ofDays(2))).build(),
                Message.builder().id("3").author("him").body("hey lala").channel("channel 3").timestamp(Instant.now().minus(Duration.ofDays(3))).build(),
                Message.builder().id("4").author("him").body("lala lalala").channel("channel 4").timestamp(Instant.now().minus(Duration.ofDays(4))).build()
        ));

        SearchCriteria criteria = new SearchCriteria();


        Instant start = Instant.now().minus(Duration.ofDays(2));
        Instant end = Instant.now().minus(Duration.ofDays(2));

        start = start.truncatedTo(ChronoUnit.DAYS);
        end = end.plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.DAYS);

        criteria.addPredicate(SearchPredicate.LOWER("timestamp", end));
        criteria.addPredicate(SearchPredicate.HIGHER("timestamp", start));

//        System.out.println(database.findByCriteria(criteria, 0, 100).stream().map(Message::getId).collect(Collectors.joining(",")));
    }
}
