package com.kpi.searchbot.services.data;

import com.kpi.searchbot.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {
    Iterable<Message> findByBodyContainingIgnoreCase(String text);
}
