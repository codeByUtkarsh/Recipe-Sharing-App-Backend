package com.feature.recipesharingapp.mapper;
import com.feature.recipesharingapp.data.entities.Ingredient;
import com.feature.recipesharingapp.data.entities.Instruction;
import com.feature.recipesharingapp.data.entities.RecipeDetails;
import com.feature.recipesharingapp.model.request.IngredientRequest;
import com.feature.recipesharingapp.model.request.InstructionRequest;
import com.feature.recipesharingapp.model.request.RecipeRequest;
import com.feature.recipesharingapp.model.response.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeMapper {




    public static RecipeDetails mapRecipeRequestToRecipeDetails(RecipeRequest recipeRequest, String userId, String role) {
        RecipeDetails recipeDetails = RecipeDetails.builder()
                .title(recipeRequest.getTitle())
                .description(recipeRequest.getDescription())
                .imageUrl(recipeRequest.getImageUrl())
                .author(recipeRequest.getAuthor())
                .type(recipeRequest.getType())
                .isDeleted(false)
                .createdAt(new Date())
                .modifiedAt(new Date())
                .userId(userId)
                .modifiedBy(role)
                .build();

        recipeDetails.setIngredients(mapIngredientRequestsToIngredients(recipeRequest.getIngredients(), recipeDetails));
        recipeDetails.setInstructions(mapInstructionRequestsToInstructions(recipeRequest.getInstructions(), recipeDetails));  // Pass the recipeDetails

        return recipeDetails;
    }



    public static Ingredient mapIngredientRequestToIngredient(IngredientRequest ingredientRequest) {
        Ingredient ingredient = new Ingredient();

        if (ingredientRequest.getIngredientId() != null) {
            ingredient.setIngredientId(ingredientRequest.getIngredientId());
        }

        ingredient.setIngredientName(ingredientRequest.getIngredientName());
        ingredient.setQuantity(ingredientRequest.getQuantity());

        return ingredient;
    }

    public static Instruction mapInstructionRequestToInstruction(InstructionRequest instructionRequest) {
        Instruction instruction = new Instruction();

        if (instructionRequest.getInstructionId() != null) {
            instruction.setInstructionId(instructionRequest.getInstructionId());
        }

        instruction.setStepNumber(instructionRequest.getStepNumber());
        instruction.setInstructionText(instructionRequest.getInstructionText());

        return instruction;
    }

    public static List<Ingredient> mapIngredientRequestsToIngredients(List<IngredientRequest> ingredientRequests, RecipeDetails recipeDetails) {
        return ingredientRequests.stream()
                .map(ingredientRequest -> {
                    Ingredient ingredient = mapIngredientRequestToIngredient(ingredientRequest);
                    ingredient.setRecipe(recipeDetails); // Associate ingredient with recipe
                    return ingredient;
                }).toList();
    }

    public static List<Instruction> mapInstructionRequestsToInstructions(List<InstructionRequest> instructionRequests, RecipeDetails recipeDetails) {
        return instructionRequests.stream()
                .map(instructionRequest -> {
                    Instruction instruction = mapInstructionRequestToInstruction(instructionRequest);
                    instruction.setRecipe(recipeDetails); // Associate instruction with recipe
                    return instruction;
                }).toList();
    }

    public static RecipeResponse mapRecipeDetailsToRecipeResponse(RecipeDetails recipeDetails) {
        List<InstructionResponse> list = new ArrayList<>();
        for (Instruction instruction : recipeDetails.getInstructions()) {
            InstructionResponse instructionResponse = mapInstructionToInstructionResponse(instruction);
            list.add(instructionResponse);
        }
        return RecipeResponse.builder()
                .userId(recipeDetails.getUserId()) // Assuming userId is 'createdBy'
                .recipeId(recipeDetails.getRecipeId())
                .title(recipeDetails.getTitle())
                .description(recipeDetails.getDescription())
                .imageUrl(recipeDetails.getImageUrl())
                .author(recipeDetails.getAuthor())
                .createdAt(recipeDetails.getCreatedAt())
                .ingredients(recipeDetails.getIngredients().stream()
                        .map(RecipeMapper::mapIngredientToIngredientResponse)
                        .toList())
                .instructions(list)
                .build();
    }


    public static IngredientResponse mapIngredientToIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .ingredientId(ingredient.getIngredientId())
                .ingredientName(ingredient.getIngredientName())
                .quantity(ingredient.getQuantity())
                .build();
    }



    public static InstructionResponse mapInstructionToInstructionResponse(Instruction instruction) {
        return InstructionResponse.builder()
                .instructionId(instruction.getInstructionId())
                .stepNumber(instruction.getStepNumber())
                .instructionText(instruction.getInstructionText())
                .build();
    }


    public static AllRecipeResponse mapRecipeDetailsToAllRecipeResponse(RecipeDetails recipeDetails){
        return AllRecipeResponse.builder()
                .recipeId(recipeDetails.getRecipeId())
                .author(recipeDetails.getAuthor())
                .description(recipeDetails.getDescription())
                .imageUrl(recipeDetails.getImageUrl())
                .title(recipeDetails.getTitle())
                .build();
    }

    public static AllRecipeResponsePage mapAllRecipeResponseToAllRecipeResponsePage(List<AllRecipeResponse> allRecipeResponses, Integer pageSize, Integer pageNumber, Long totalCount, Integer totalPages){
        return AllRecipeResponsePage.builder()
                .allRecipeResponseList(allRecipeResponses)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .build();
    }
}