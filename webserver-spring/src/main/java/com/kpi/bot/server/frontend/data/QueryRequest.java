package com.kpi.bot.server.frontend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryRequest {
    private String query;
    private Long offset;
    private Long limit;
}
