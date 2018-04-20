package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.server.frontend.data.SearchRequest;
import com.kpi.bot.utils.StringUtils;
import com.kpi.bot.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Object findByBody(@RequestBody SearchRequest params) {
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

        return ResponseBuilder.OK().add("messages", service.search(criteria, offset, limit)).build();
    }

}
