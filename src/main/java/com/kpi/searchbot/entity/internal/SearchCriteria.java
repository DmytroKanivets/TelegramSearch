package com.kpi.searchbot.entity.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kpi.searchbot.entity.internal.Message;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCriteria {
    private String body;
    private String author;
    private String channel;

    private Specification<Message> body() {
        return (root, criteriaQuery, criteriaBuilder) -> body == null ? null : criteriaBuilder.like(root.get("body"), "%" + body + "%");
    }

    private Specification<Message> channel() {
        return (root, criteriaQuery, criteriaBuilder) -> channel == null ? null : criteriaBuilder.like(root.get("channel"), "%" + channel + "%");
    }

    private Specification<Message> author() {
        return (root, criteriaQuery, criteriaBuilder) -> author == null ? null : criteriaBuilder.like(root.get("author"), "%" + author + "%");
    }

    public Specification<Message> buildSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Specification<Message>> specifications = new LinkedList<>();

            specifications.add(body());
            specifications.add(channel());
            specifications.add(author());

            return specifications.stream().filter(Objects::nonNull).reduce((specification, specification2) -> {
                if (specification == null) {
                    return specification2;
                } else {
                    return Specifications.where(specification).and(specification2);
                }
            }).flatMap(messageSpecification -> Optional.ofNullable(messageSpecification == null ? null : messageSpecification.toPredicate(root, criteriaQuery, criteriaBuilder))).get();
        };
    }
}
