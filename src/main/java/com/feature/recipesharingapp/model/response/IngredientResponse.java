package com.feature.recipesharingapp.model.response;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class IngredientResponse {
    private Integer ingredientId;
    private String ingredientName;
    private String quantity;
}
