package com.kpi.bot.server.frontend.data.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {
    private static final Integer defaultOffset = 0;
    private static final Integer defaultLimit = 100;

    private Integer offset;
    private Integer limit;

    public Integer getOffset() {
        if (offset != null && offset >= 0) {
            return offset;
        } else {
            return defaultOffset;
        }
    }

    public Integer getLimit() {
        if (limit != null && limit < defaultLimit && limit > 0) {
            return limit;
        } else {
            return defaultLimit;
        }
    }
}
