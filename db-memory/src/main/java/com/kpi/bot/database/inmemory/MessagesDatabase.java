package com.kpi.bot.database.inmemory;

import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.entity.search.SearchType;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessagesDatabase extends Database<Message> implements SearchableRepository<Message> {

    @Override
    public Message save(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        return super.save(message);
    }

    private Predicate<String> createStringPredicate(SearchPredicate predicate) {
        return s -> {
            if (predicate.getValue() == null) {
                return (predicate.getType().equals(SearchType.EQUALS) || predicate.getType().equals(SearchType.LIKE))
                    && s == null;
            }

            if (s == null) {
                return false;
            }

            switch (predicate.getType()) {
                case EQUALS:
                    return predicate.getValue().toString().equals(s);
                case HIGHER:
                    return predicate.getValue().toString().compareTo(s) < 0;
                case LOWER:
                    return predicate.getType().toString().compareTo(s) > 0;
                case LIKE:
                    if (predicate.getValue() instanceof Pattern) {
                        return ((Pattern) predicate.getValue()).matcher(s).matches();
                    } else {
                        return s.contains(predicate.getValue().toString());
                    }
                default:
                    throw new RuntimeException("Can not find matching type for " + predicate.getType());
            }
        };
    }

    private Predicate<Message> buildPredicate(SearchCriteria criteria) {
        Predicate<Message> result = (s) -> true;

        for (SearchPredicate predicate : criteria.getPredicatesForField("id")) {
            result = result.and((m) -> createStringPredicate(predicate).test(m.getId()));
        }

        for (SearchPredicate predicate : criteria.getPredicatesForField("author")) {
            result = result.and((m) -> createStringPredicate(predicate).test(m.getAuthor()));
        }

        for (SearchPredicate predicate : criteria.getPredicatesForField("channel")) {
            result = result.and((m) -> createStringPredicate(predicate).test(m.getChannel()));
        }

        for (SearchPredicate predicate : criteria.getPredicatesForField("body")) {
            result = result.and((m) -> createStringPredicate(predicate).test(m.getBody()));
        }

        return result;
    }

    @Override
    public List<Message> findByCriteria(SearchCriteria criteria) {
        return findAll().stream().filter(buildPredicate(criteria)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findByCriteria(SearchCriteria criteria, Long offset, Long limit) {
        return findAll().stream().filter(buildPredicate(criteria)).skip(offset).limit(limit).collect(Collectors.toList());
    }

}
