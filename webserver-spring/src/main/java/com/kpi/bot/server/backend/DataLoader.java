package com.kpi.bot.server.backend;

import com.kpi.bot.entity.data.Message;
import com.kpi.bot.server.backend.entity.Comment;
import com.kpi.bot.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Component
public class DataLoader {
    private static final String DATA_LOCATION = "https://jsonplaceholder.typicode.com/comments";

    private MessageService service;

    @Autowired
    public DataLoader(MessageService service) {
        this.service = service;
    }

    public void loadData() {
        service.deleteAll();

        Comment[] comments = new RestTemplate().getForObject(DATA_LOCATION, Comment[].class);
        List<Message> messages = new LinkedList<>();
        for (Comment comment : comments) {
            messages.add(new Message(String.valueOf(comment.getId()), comment.getName(), comment.getEmail(), comment.getBody().replace("\n", "")));
        }
        service.indexAll(messages);
    }
}
