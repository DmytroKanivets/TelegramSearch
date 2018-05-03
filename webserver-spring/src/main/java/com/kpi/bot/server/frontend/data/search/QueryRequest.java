package com.kpi.bot.server.frontend.data.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryRequest extends SearchRequest {
    private String query;
}
