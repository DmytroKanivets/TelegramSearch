package com.kpi.bot.entity.search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchPredicate {
    private String field;
    private SearchType type;
    private Object value;
    private boolean match;

    public static SearchPredicate EQUALS(String field, Object object) {
        return new SearchPredicate(field, SearchType.EQUALS, object, true);
    }

    public static SearchPredicate NOT_EQUALS(String field, Object object) {
        return new SearchPredicate(field, SearchType.EQUALS, object, false);
    }

    public static SearchPredicate LOWER(String field, Object object) {
        return new SearchPredicate(field, SearchType.LOWER, object, true);
    }

    public static SearchPredicate HIGHER(String field, Object object) {
        return new SearchPredicate(field, SearchType.HIGHER, object, true);
    }

    public static SearchPredicate LIKE(String field, Object object) {
        return new SearchPredicate(field, SearchType.LIKE, object, true);
    }

    public static SearchPredicate CONTAINS(String field, Object object) {
        return new SearchPredicate(field, SearchType.CONTAINS, object, true);
    }

    public static SearchPredicate NOT_CONTAINS(String field, Object object) {
        return new SearchPredicate(field, SearchType.CONTAINS, object, false);
    }
}
