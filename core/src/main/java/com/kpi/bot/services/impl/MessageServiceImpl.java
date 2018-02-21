package com.kpi.bot.services.impl;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.services.MessageService;

import java.util.List;

public class MessageServiceImpl implements MessageService {

    private Repository<Message> repository;

    public MessageServiceImpl(Repository<Message> repository) {
        this.repository = repository;
    }

    @Override
    public void index(Message message) {
        repository.save(message);
    }

    @Override
    public void indexAll(Iterable<Message> messages) {
        repository.saveAll(messages);
    }

    @Override
    public List<Message> search(SearchCriteria criteria) {
        return repository.findByCriteria(criteria);
    }

    @Override
    public List<Message> getAll() {
        return repository.findAll();
    }

    @Override
    public Message getById(String id) {
        return repository.find(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}