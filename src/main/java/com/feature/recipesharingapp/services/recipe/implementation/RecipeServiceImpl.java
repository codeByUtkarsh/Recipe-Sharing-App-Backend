package com.feature.recipesharingapp.services.recipe.implementation;

import com.feature.recipesharingapp.config.JwtHelper;
import com.feature.recipesharingapp.data.entities.Ingredient;
import com.feature.recipesharingapp.data.entities.Instruction;
import com.feature.recipesharingapp.data.entities.RecipeDetails;
import com.feature.recipesharingapp.data.repos.IngredientRepository;
import com.feature.recipesharingapp.data.repos.RecipeRepository;
import com.feature.recipesharingapp.data.specification.Recipe;
import com.feature.recipesharingapp.data.specification.Sorting;
import com.feature.recipesharingapp.exception.ApplicationErrorCode;
import com.feature.recipesharingapp.exception.CustomException;
import com.feature.recipesharingapp.mapper.RecipeMapper;
import com.feature.recipesharingapp.model.request.*;
import com.feature.recipesharingapp.model.response.*;
import com.feature.recipesharingapp.services.recipe.RecipeService;
import com.feature.recipesharingapp.services.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;




/**
 * Implementation of the RecipeService interface.
 */
@Service
@Slf4j
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserService userService;
    private final JwtHelper helper;
    private final IngredientRepository ingredientRepository;




    /**
     * Saves a new recipe.
     * @param recipeRequest The request object containing recipe details.
     * @return The response object containing saved recipe details.
     */
    @Override
    public RecipeResponse save(RecipeRequest recipeRequest) {
        log.info("Saving new Recipe");
        String role = getRoleNameAndValidate();
        String userId = helper.getUserId();
        RecipeDetails recipeDetails = RecipeMapper.mapRecipeRequestToRecipeDetails(recipeRequest, userId, role);

        recipeRepository.save(recipeDetails);
        log.info("Recipe with ID {} successfully saved.", recipeDetails.getRecipeId());
        return RecipeMapper.mapRecipeDetailsToRecipeResponse(recipeDetails);
    }



    /**
     * Updates an existing recipe.
     * @param recipeUpdateRequest The request object containing updated recipe details.
     * @param id The ID of the recipe to update.
     * @return The response object containing updated recipe details.
     */

    @Override
    public RecipeResponse update(RecipeUpdateRequest recipeUpdateRequest, Integer id) {
        RecipeDetails recipeDetails = recipeRepository.findByRecipeIdAndIsDeleted(id, false);

        if (recipeDetails == null) {
            throw new CustomException(ApplicationErrorCode.RECIPE_NOT_FOUND);
        }

        updateRecipeDetails(recipeDetails, recipeUpdateRequest);
        updateIngredients(recipeDetails, recipeUpdateRequest.getIngredients());
        updateInstructions(recipeDetails, recipeUpdateRequest.getInstructions());

        recipeRepository.save(recipeDetails);

        return RecipeMapper.mapRecipeDetailsToRecipeResponse(recipeDetails);
    }

    private void updateRecipeDetails(RecipeDetails recipeDetails, RecipeUpdateRequest request) {
        recipeDetails.setTitle(request.getTitle());
        recipeDetails.setDescription(request.getDescription());
        recipeDetails.setImageUrl(request.getImageUrl());
    }

    private void updateIngredients(RecipeDetails recipeDetails, List<IngredientRequest> ingredientRequests) {
        recipeDetails.getIngredients().clear();

        for (IngredientRequest ingredientRequest : ingredientRequests) {
            Ingredient ingredient = RecipeMapper.mapIngredientRequestToIngredient(ingredientRequest);
            ingredient.setRecipe(recipeDetails);
            recipeDetails.getIngredients().add(ingredient);
        }
    }

    private void updateInstructions(RecipeDetails recipeDetails, List<InstructionRequest> instructionRequests) {
        recipeDetails.getInstructions().clear();

        for (InstructionRequest instructionRequest : instructionRequests) {
            Instruction instruction = RecipeMapper.mapInstructionRequestToInstruction(instructionRequest);
            instruction.setRecipe(recipeDetails);
            recipeDetails.getInstructions().add(instruction);
        }
    }



    /**
     * Retrieves a recipe by its ID.
     * @param id The ID of the recipe to retrieve.
     * @return The response object containing recipe details.
     */

    @Override
    public RecipeResponse getById(Integer id) {
        log.info("Getting Recipe with recipe ID: {}", id);
        RecipeDetails recipeDetails = recipeRepository.findByRecipeIdAndIsDeleted(id, false);
        if (recipeDetails == null) {
            log.error("Recipe not found with Id : {}", id);
            throw new CustomException(ApplicationErrorCode.RECIPE_NOT_FOUND);
        }
        return RecipeMapper.mapRecipeDetailsToRecipeResponse(recipeDetails);
    }



    /**
     * Deletes a recipe by its ID.
     * @param id The ID of the recipe to delete.
     * @return The response object indicating deletion status.
     */
    @Override
    public DeleteResponse delete(Integer id) {
        log.info("Deleting Recipe with ID: {}", id);

        RecipeDetails recipeDetails = recipeRepository.findByRecipeIdAndIsDeleted(id, false);
        if (recipeDetails == null) {
            log.error("Recipe not found with Id : {}", id);
            throw new CustomException(ApplicationErrorCode.RECIPE_NOT_FOUND);
        }
        recipeDetails.setIsDeleted(true);
        recipeRepository.save(recipeDetails);
        return new DeleteResponse("Recipe is Successfully deleted from the database");
    }



    /**
     * Retrieves all recipes based on the query parameters.
     * @param recipeQuery The query parameters for retrieving recipes.
     * @return The response object containing a page of recipes.
     */
    @Override
    public AllRecipeResponsePage getAll(RecipeQuery recipeQuery) {
        log.info("Building search specification with criteria - Title : {}, Type : {}, Ingredient : {}",
                recipeQuery.getTitle(), recipeQuery.getType(), recipeQuery.getIngredient());
        validatePageNumberAndSize(recipeQuery);

        Specification<RecipeDetails> specification = Recipe.searchRecipe(recipeQuery);
        Sort sort = Sorting.sorting(recipeQuery.getSortBy(), recipeQuery.getSortOrder());

        Pageable pageable = PageRequest.of(recipeQuery.getPageNumber(), recipeQuery.getPageSize(), sort);
        Page<RecipeDetails> recipePage = recipeRepository.findAll(specification, pageable);

        long totalCount = recipeRepository.count();

        return allRecipeResponsePage(recipePage.getContent(),
                recipeQuery.getPageSize(),
                recipeQuery.getPageNumber(),
                totalCount,
                recipePage.getTotalPages()
        );
    }

    private AllRecipeResponsePage allRecipeResponsePage(List<RecipeDetails> allRecipes, Integer pageSize, Integer pageNumber, Long totalCount, Integer totalPages) {
        log.info("Building AllRecipeResponsePage. PageSize: {}, PageNumber: {}, TotalCount: {}, TotalPages: {}", pageSize, pageNumber, totalCount, totalPages);

        List<AllRecipeResponse> allRecipeResponse = new ArrayList<>();
        for (RecipeDetails recipe : allRecipes) {
            log.info("Mapping recipe details for recipeId: {}", recipe.getRecipeId());
            AllRecipeResponse recipeResponse = RecipeMapper.mapRecipeDetailsToAllRecipeResponse(recipe);
            allRecipeResponse.add(recipeResponse);
        }
        log.info("Successfully built allRecipeResponse list. Mapping to response page.");
        return RecipeMapper.mapAllRecipeResponseToAllRecipeResponsePage(allRecipeResponse, pageSize, pageNumber, totalCount, totalPages);
    }


    /**
     * Validates the page number and page size for pagination.
     * @param recipeQuery The query parameters containing page number and size.
     */
    public void validatePageNumberAndSize(RecipeQuery recipeQuery) {
        log.info("Validating page number and page size. PageNumber: {}, PageSize: {}", recipeQuery.getPageNumber(), recipeQuery.getPageSize());
        if (recipeQuery.getPageNumber() == null || recipeQuery.getPageNumber() < 0) {
            log.info("Invalid page number: {}", recipeQuery.getPageNumber());
            throw new CustomException(ApplicationErrorCode.INVALID_PAGE_NUMBER);
        }
        if (recipeQuery.getPageSize() == null || recipeQuery.getPageSize() <= 0) {
            log.info("Invalid or null page size: {}. Setting default page size to 5.", recipeQuery.getPageSize());
            throw new CustomException(ApplicationErrorCode.INVALID_PAGE_SIZE);
        } else {
            recipeQuery.setPageSize(12);
        }
        log.info("Page number and page size validation passed. PageNumber: {}, PageSize: {}", recipeQuery.getPageNumber(), recipeQuery.getPageSize());
    }



    /**
     * Retrieves and validates the role of the user.
     * @return The role of the user.
     */
    @Override
    public String getRoleNameAndValidate() {
        log.info("Getting user role and validating.");

        String userId = helper.getUserId();
        log.info("Fetched userId: {}", userId);

        UserResponse userResponse = userService.getUserId(userId);
        String role = userResponse.getRole();

        log.info("Validating role: {}", role);
        validateRole(role);

        log.info("Role validation successful. Role: {}", role);
        return role;
    }

    private void validateRole(String role) {
        log.error("RoleName {} is not valid", role);
        if (!role.equalsIgnoreCase("user")) {
            throw new CustomException(ApplicationErrorCode.INVALID_ROLE);
        }
    }
}
