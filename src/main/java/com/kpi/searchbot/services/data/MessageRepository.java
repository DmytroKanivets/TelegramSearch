package com.kpi.searchbot.services.data;

import com.kpi.searchbot.entity.internal.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageRepository extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {
    Iterable<Message> findByBodyContainingIgnoreCase(String text);
}
