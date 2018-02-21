package com.kpi.bot.entity.search;

import java.util.*;

public class SearchCriteria {
    private Map<String, List<SearchPredicate>> predicates = new HashMap<>();

    public void addPredicate(SearchPredicate predicate) {
        predicates.computeIfAbsent(predicate.getField(), k -> new LinkedList<>());
        predicates.get(predicate.getField()).add(predicate);
    }

    public List<SearchPredicate> getPredicates() {
        List<SearchPredicate> result = new LinkedList<>();
        for (List<SearchPredicate> predicateList : predicates.values()) {
            result.addAll(predicateList);
        }
        return result;
    }

    public List<SearchPredicate> getPredicatesForField(String field) {
        if (predicates.get(field) == null) {
            return Collections.emptyList();
        } else {
            return new LinkedList<>(predicates.get(field));
        }
    }
}
