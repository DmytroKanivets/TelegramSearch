package com.kpi.bot.server.backend.entity;

import lombok.Data;

@Data
public class Comment {
    Long postId;
    Long id;
    String name;
    String email;
    String body;
}
