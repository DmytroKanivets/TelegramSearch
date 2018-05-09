package com.kpi.bot.services.impl;

import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.services.MessageService;
import com.kpi.bot.services.Statistics;
import com.kpi.bot.stats.IndexingStatistics;

import java.util.List;

public class MessageServiceImpl implements MessageService {

    private SearchableRepository<Message> repository;

    public MessageServiceImpl(SearchableRepository<Message> repository) {
        this.repository = repository;
    }

    @Override
    public void index(Message message) {
        long start = System.currentTimeMillis();
        repository.save(message);
        long end = System.currentTimeMillis();
        IndexingStatistics.messageIndexed(message.getChannel(), end - start);
    }

    @Override
    public void updateMessage(Message message) {
        repository.save(message);
    }

    @Override
    public void indexAll(Iterable<Message> messages) {
        messages.forEach(this::index);
    }

    @Override
    public List<Message> search(SearchCriteria criteria, Integer offset, Integer limit) {

        long start = System.currentTimeMillis();
        List<Message> messages = repository.findByCriteria(criteria, offset, limit);
        long end = System.currentTimeMillis();
        Statistics.addTime(Statistics.SEARCH, end - start);
        return messages;
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
