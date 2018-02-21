package com.kpi.bot.data;

import com.kpi.bot.entity.search.SearchCriteria;

import java.util.List;

public interface Repository<T> {
    Iterable<T> findByBody(String text);
    T save(T message);
    List<T> saveAll(Iterable<T> messages);
    T find(String key);
    List<T> findAll();
    List<T> findByCriteria(SearchCriteria criteria);
    void delete(String key);
    void deleteAll();
}
