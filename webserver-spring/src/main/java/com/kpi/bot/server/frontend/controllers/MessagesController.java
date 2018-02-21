package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.server.frontend.data.SearchParams;
import com.kpi.bot.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessagesController {
    private MessageService service;

    @Autowired
    public MessagesController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/")
    public void create(@RequestBody Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        service.index(message);
    }

    private boolean notEmpty(String s) {
        return s != null && s.length() > 0;
    }

    @PostMapping("/")
    public Iterable<Message> findByBody(@RequestBody SearchParams params) {
        SearchCriteria criteria = new SearchCriteria();
        if (notEmpty(params.getBody())) {
            criteria.addPredicate(SearchPredicate.LIKE("body", params.getBody()));
        }
        if (notEmpty(params.getAuthor())) {
            criteria.addPredicate(SearchPredicate.EQUALS("author", params.getAuthor()));
        }
        if (notEmpty(params.getChannel())) {
            criteria.addPredicate(SearchPredicate.EQUALS("channel", params.getChannel()));
        }
        return service.search(criteria);
    }

}
