package com.kpi.bot.server.frontend.data;

import lombok.Data;

import java.time.Instant;

@Data
public class JoinChannelRequest {
    private String channel;
    private Instant startIndexDate;
}
