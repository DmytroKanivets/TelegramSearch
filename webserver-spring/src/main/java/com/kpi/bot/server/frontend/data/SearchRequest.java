package com.kpi.bot.server.frontend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {
    private String body;
    private String author;
    private String channel;
    private Long offset;
    private Long limit;
}
