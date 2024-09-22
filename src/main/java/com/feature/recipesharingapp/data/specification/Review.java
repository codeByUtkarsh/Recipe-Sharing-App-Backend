package com.feature.recipesharingapp.data.specification;


import com.feature.recipesharingapp.data.entities.ReviewDetails;
import com.feature.recipesharingapp.model.request.ReviewQuery;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Review {
    public static Specification<ReviewDetails> searchReview(ReviewQuery reviewQuery) {
        log.info("Building search specification with criteria - review : {}" , reviewQuery.getReview());
        Specification<ReviewDetails> spec = Specification.where(null);

        spec = spec.and(reviewIsPresent());

        spec = (reviewQuery.getReview() != null && !reviewQuery.getReview().isEmpty()) ? spec.and(createFilter(reviewQuery.getReview())) : spec;

        log.info("Built specification successfully.");
        return spec;
    }

    private static Specification<ReviewDetails> createFilter(String value) {
        log.info("Creating filter for attribute: {} with value: {}", "review", value);
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("review"), "%" + value + "%");
    }

    private static Specification<ReviewDetails> reviewIsPresent() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }


}
