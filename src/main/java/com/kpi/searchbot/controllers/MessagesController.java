package com.kpi.searchbot.controllers;

import com.kpi.searchbot.entity.internal.Message;
import com.kpi.searchbot.entity.internal.SearchCriteria;
import com.kpi.searchbot.services.data.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessagesController {
    private MessageRepository repository;

    @Autowired
    public MessagesController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable String id) {
        return repository.findOne(id);
    }

    @PutMapping("/")
    public void create(@RequestBody Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        repository.save(message);
    }

    @PostMapping("/")
    public Iterable<Message> findByBody(@RequestBody SearchCriteria body) {
        return repository.findAll(body.buildSpecification());
    }

}
