package com.kpi.bot.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Identifiable {
    private String id;
    private String channel;
    private String author;
    private String body;
    private Instant timestamp;
}