package com.feature.recipesharingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientRequest {



    @NotNull(message = "Ingredient cannot be null.")
    private Integer ingredientId;

    @NotBlank(message = "Ingredient name is mandatory")
    @Size(max = 255, message = "Ingredient name cannot exceed 255 characters")
    private String ingredientName;

    @NotBlank(message = "Quantity is mandatory")
    @Size(max = 50, message = "Quantity cannot exceed 50 characters")
    private String quantity;
}
