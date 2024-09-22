package com.feature.recipesharingapp.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllRecipeResponse {
    private Integer recipeId;
    private String title;
    private String description;
    private String imageUrl;
    private String author;
}
