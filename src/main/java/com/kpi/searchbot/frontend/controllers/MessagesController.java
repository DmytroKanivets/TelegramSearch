package com.kpi.searchbot.frontend.controllers;

import com.kpi.searchbot.entity.Message;
import com.kpi.searchbot.services.data.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessagesController {
    private MessageRepository repository;

    @Autowired
    public MessagesController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Iterable<Message> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable String id) {
        return repository.findOne(id);
    }

    @PutMapping("/")
    public void create(@RequestBody Message message) {
        if (message.getId() == null) {
            message.setId(String.valueOf(message.hashCode()));
        }
    }

    @GetMapping("/search")
    public Iterable<Message> findByBody(@RequestParam("body") String body) {
        return repository.findByBodyContainingIgnoreCase(body);
    }

}
