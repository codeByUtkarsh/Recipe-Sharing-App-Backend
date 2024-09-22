package com.feature.recipesharingapp.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private String userId;
    private String reviewerName;
    private String comment;
    private Integer rating;
}
