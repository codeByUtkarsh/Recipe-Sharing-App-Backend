package com.feature.recipesharingapp.model.response;


import lombok.Builder;
import lombok.Data;


import java.util.Date;
import java.util.List;


@Data
@Builder
public class RecipeResponse {
    private String userId;
    private Integer recipeId;
    private String title;
    private String description;
    private String imageUrl;
    private String author;
    private Date createdAt;
    private List<IngredientResponse> ingredients;
    private List<InstructionResponse> instructions;
}
