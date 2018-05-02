package com.kpi.bot.database.inmemory;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Identifiable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database<T extends Identifiable> implements Repository<T> {

    private Map<String, T> storage = new ConcurrentHashMap<>();

    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public List<T> saveAll(Iterable<T> messages) {
        List<T> result = new LinkedList<>();
        for (T message : messages) {
            result.add(save(message));
        }
        return result;
    }

    public T find(String key) {
        return storage.get(key);
    }

    public List<T> findAll() {
        return new LinkedList<>(storage.values());
    }

    @Override
    public void delete(String key) {
        storage.remove(key);
    }

    @Override
    public void deleteAll() {

    }
}
