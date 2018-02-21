package com.kpi.bot.entity.search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchPredicate {
    private String field;
    private SearchType type;
    private Object value;

    public static SearchPredicate EQUALS(String field, Object object) {
        return new SearchPredicate(field, SearchType.EQUALS, object);
    }

    public static SearchPredicate LOWER(String field, Object object) {
        return new SearchPredicate(field, SearchType.LOWER, object);
    }

    public static SearchPredicate HIGHER(String field, Object object) {
        return new SearchPredicate(field, SearchType.HIGHER, object);
    }

    public static SearchPredicate LIKE(String field, Object object) {
        return new SearchPredicate(field, SearchType.LIKE, object);
    }
}
