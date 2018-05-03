package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.server.frontend.data.QueryRequest;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.server.frontend.data.SearchRequest;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/message")
public class MessagesController {
    private static final long MAX_MESSAGES_SIZE = 100;

    private MessageService service;

    @Autowired
    public MessagesController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/params")
    public Object findByParams(@RequestBody SearchRequest params) {
        SearchCriteria criteria = new SearchCriteria();
        if (StringUtils.notEmpty(params.getBody())) {
            criteria.addPredicate(SearchPredicate.LIKE("body", params.getBody()));
        }
        if (StringUtils.notEmpty(params.getAuthor())) {
            criteria.addPredicate(SearchPredicate.EQUALS("author", params.getAuthor()));
        }
        if (StringUtils.notEmpty(params.getChannel())) {
            criteria.addPredicate(SearchPredicate.EQUALS("channel", params.getChannel()));
        }

        long offset = 0;
        if (params.getOffset() != null && params.getOffset() >= 0) {
            offset = params.getOffset();
        }

        long limit = MAX_MESSAGES_SIZE;
        if (params.getLimit() != null && params.getLimit() > 0 && params.getLimit() <= MAX_MESSAGES_SIZE) {
            limit = params.getLimit();
        }

        if (params.getStartDate() != null) {
            System.out.println("add start");
            criteria.addPredicate(SearchPredicate.HIGHER("timestamp", params.getStartDate().truncatedTo(ChronoUnit.DAYS)));
        }

        if (params.getEndDate() != null) {
            System.out.println("add end");
            criteria.addPredicate(SearchPredicate.LOWER("timestamp", params.getEndDate().plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.DAYS)));
        }

        return ResponseBuilder.OK().add("messages", service.search(criteria, offset, limit)).build();
    }

    @PostMapping("/query")
    public Object findByParams(@RequestBody QueryRequest params) {
        long offset = 0;
        if (params.getOffset() != null && params.getOffset() >= 0) {
            offset = params.getOffset();
        }

        long limit = MAX_MESSAGES_SIZE;
        if (params.getLimit() != null && params.getLimit() > 0 && params.getLimit() <= MAX_MESSAGES_SIZE) {
            limit = params.getLimit();
        }

        return ResponseBuilder.OK().add("messages", service.search(params.getQuery(), offset, limit)).build();
    }
}
