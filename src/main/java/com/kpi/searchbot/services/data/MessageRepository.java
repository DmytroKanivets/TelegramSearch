package com.kpi.searchbot.services.data;

import com.kpi.searchbot.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, String> {
    Iterable<Message> findByBodyContainingIgnoreCase(String text);
}
