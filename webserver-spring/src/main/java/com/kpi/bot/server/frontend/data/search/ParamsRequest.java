package com.kpi.bot.server.frontend.data.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamsRequest extends SearchRequest {
    private String body;
    private String author;
    private String channel;
    private Instant startDate;
    private Instant endDate;
}
