package com.feature.recipesharingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import java.util.List;


@Data
@Builder
public class RecipeRequest {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(
            regexp = "^data:image/(jpeg|png|gif|bmp|webp);base64,[a-zA-Z0-9+/=]+$",
            message = "Invalid Base64 image format"
    )
    private String imageUrl;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 100, message = "Author name cannot exceed 100 characters")
    private String author;

    @NotNull(message = "Ingredients list cannot be null")
    private List<IngredientRequest> ingredients;

    @NotNull(message = "Instructions list cannot be null")
    private List<InstructionRequest> instructions;
}
