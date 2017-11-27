package com.kpi.searchbot.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
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
