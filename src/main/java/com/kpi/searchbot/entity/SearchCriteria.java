package com.kpi.searchbot.entity;

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
import java.util.function.BinaryOperator;
import java.util.function.Function;

@Data
public class SearchCriteria {
    private String body;

    private Specification<Message> body() {
        return new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return body == null ? null : criteriaBuilder.like(root.get("body"), "%" + body + "%");
            }
        };
    }

    public Specification<Message> buildSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Specification<Message>> specifications = new LinkedList<>();

            specifications.add(body());

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
