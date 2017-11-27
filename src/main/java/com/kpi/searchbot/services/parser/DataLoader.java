package com.kpi.searchbot.services.parser;

import com.kpi.searchbot.entity.external.Comment;
import com.kpi.searchbot.entity.internal.Message;
import com.kpi.searchbot.services.data.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.awt.datatransfer.DataTransferer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataLoader {
    private static final String DATA_LOCATION = "https://jsonplaceholder.typicode.com/comments";

    private MessageRepository repository;

    @Autowired
    public DataLoader(MessageRepository repository) {
        this.repository = repository;
    }

    public void loadData() {
        repository.deleteAll();

        repository.save(Arrays.stream(new RestTemplate().getForObject(DATA_LOCATION, Comment[].class)).map(
                comment -> new Message(String.valueOf(comment.getId()), comment.getName(), comment.getEmail(), comment.getBody().replace("\n", ""))
        ).collect(Collectors.toList()));
    }
}
