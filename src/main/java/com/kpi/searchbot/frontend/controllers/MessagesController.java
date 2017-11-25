package com.kpi.searchbot.frontend.controllers;

import com.kpi.searchbot.entity.Message;
import com.kpi.searchbot.services.data.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/message")
public class MessagesController {
    private MessageRepository repository;

    @Autowired
    public MessagesController(MessageRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/all")
    public Iterable<Message> getAll() {
        return repository.findAll();
    }


}
