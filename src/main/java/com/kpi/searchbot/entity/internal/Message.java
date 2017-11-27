package com.kpi.searchbot.entity.internal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;
    private String channel;
    private String author;
    private String body;
    /*
    List<String> tags;
     */
}
