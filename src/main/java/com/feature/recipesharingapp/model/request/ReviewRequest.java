package com.feature.recipesharingapp.model.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ReviewRequest {


    @NotBlank(message = "Review text is mandatory")
    @Size(max = 2000, message = "Review text cannot exceed 2000 characters")
    private String reviewText;

    @NotNull(message = "Rating is mandatory")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
}
