package com.kpi.bot.entity.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Identifiable {
    private String id;
    private String channel;
    private String author;
    private String body;
    private Instant timestamp;
}