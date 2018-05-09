package com.kpi.bot.services;

import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;

import java.util.List;

public interface MessageService {
    void index(Message message);
    void updateMessage(Message message);
    void indexAll(Iterable<Message> messages);

    List<Message> search(SearchCriteria criteria, Integer offset, Integer limit);
    List<Message> getAll();
    Message getById(String id);

    void delete(String id);
    void deleteAll();
}
