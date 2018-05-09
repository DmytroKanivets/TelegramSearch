package com.kpi.bot.server.frontend.controllers;

import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.server.frontend.data.search.ParamsRequest;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/message")
public class UserMessagesController {

    private MessageService service;

    @Autowired
    public UserMessagesController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable String id) {
        return service.getById(id);
    }

    private SearchCriteria buildCriteria(ParamsRequest params) {
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

        if (params.getStartDate() != null) {
            criteria.addPredicate(SearchPredicate.HIGHER("timestamp", params.getStartDate().truncatedTo(ChronoUnit.DAYS)));
        }

        if (params.getEndDate() != null) {
            criteria.addPredicate(SearchPredicate.LOWER("timestamp", params.getEndDate().plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.DAYS)));
        }

        return criteria;
    }

    @PostMapping("/params")
    public Object findByParams(@RequestBody ParamsRequest params) {
        return ResponseBuilder.OK().add("messages", service.search(buildCriteria(params), params.getOffset(), params.getLimit())).build();
    }

}
