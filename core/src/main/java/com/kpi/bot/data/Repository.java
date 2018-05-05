package com.kpi.bot.data;


import com.kpi.bot.entity.data.Identifiable;

import java.util.List;

public interface Repository <T extends Identifiable> {
    T save(T entity);
    List<T> saveAll(Iterable<T> entities);
    T find(String key);
    List<T> findAll();
    void delete(String key);
    void deleteAll();
    int getSize();
}
