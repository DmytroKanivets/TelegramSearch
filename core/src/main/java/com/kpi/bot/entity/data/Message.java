package com.kpi.bot.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id;
    private String channel;
    private String author;
    private String body;
}
