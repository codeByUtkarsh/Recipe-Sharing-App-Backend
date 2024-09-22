package com.feature.recipesharingapp.controllers;

import com.feature.recipesharingapp.model.request.RecipeQuery;
import com.feature.recipesharingapp.model.request.RecipeRequest;
import com.feature.recipesharingapp.model.request.RecipeUpdateRequest;
import com.feature.recipesharingapp.model.response.*;
import com.feature.recipesharingapp.services.recipe.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling recipe-related operations.
 * Provides endpoints for creating, updating, retrieving, and deleting recipes.
 */
@Validated
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Adds a new recipe.
     *
     * @param addRecipeRequest the request object containing the recipe details to be added.
     * @return ResponseEntity<RecipeResponse> containing the details of the added recipe.
     */
    @PostMapping
    public ResponseEntity<RecipeResponse> save(@Valid @RequestBody RecipeRequest addRecipeRequest) {
        log.info("Received request to add a new recipe.");
        RecipeResponse addRecipeResponse = recipeService.save(addRecipeRequest);
        log.info("New recipe added successfully.");
        return new ResponseEntity<>(addRecipeResponse, HttpStatus.CREATED);
    }

    /**
     * Updates an existing recipe.
     *
     * @param recipeUpdateRequest the request object containing updated recipe details.
     * @param id the ID of the recipe to be updated.
     * @return ResponseEntity<RecipeResponse> containing the updated recipe details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> update(@Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest,
                                                 @PathVariable("id") Integer id) {
        log.info("Received request to update recipe with ID: {}", id);
        RecipeResponse updateRecipeResponse = recipeService.update(recipeUpdateRequest, id);
        log.info("Recipe with ID {} updated successfully.", id);
        return new ResponseEntity<>(updateRecipeResponse, HttpStatus.OK);
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to be retrieved.
     * @return ResponseEntity<RecipeResponse> containing the details of the retrieved recipe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable("id") Integer id) {
        log.info("Received request to retrieve details of recipe with ID: {}", id);
        RecipeResponse recipeResponse = recipeService.getById(id);
        log.info("Details of recipe with ID {} retrieved successfully.", id);
        return new ResponseEntity<>(recipeResponse, HttpStatus.OK);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to be deleted.
     * @return ResponseEntity<DeleteResponse> containing the result of the delete operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable("id") Integer id) {
        log.info("Received request to delete recipe with ID: {}", id);
        DeleteResponse deleteResponse = recipeService.delete(id);
        log.info("Recipe with ID {} deleted successfully.", id);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    /**
     * Retrieves all recipes based on the provided query parameters.
     *
     * @param recipeQuery the query parameters for filtering and pagination.
     * @return ResponseEntity<AllRecipeResponsePage> containing the paginated list of recipes.
     */
    @GetMapping
    public ResponseEntity<AllRecipeResponsePage> getAll(@ModelAttribute RecipeQuery recipeQuery) {
        log.info("Received request to retrieve all recipes.");
        AllRecipeResponsePage allRecipeResponsePage = recipeService.getAll(recipeQuery);
        log.info("Retrieved all recipes successfully.");
        return new ResponseEntity<>(allRecipeResponsePage, HttpStatus.OK);
    }
}
