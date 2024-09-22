package com.feature.recipesharingapp.services.recipe;
import com.feature.recipesharingapp.model.request.RecipeQuery;
import com.feature.recipesharingapp.model.request.RecipeRequest;
import com.feature.recipesharingapp.model.request.RecipeUpdateRequest;
import com.feature.recipesharingapp.model.response.AllRecipeResponsePage;
import com.feature.recipesharingapp.model.response.DeleteResponse;
import com.feature.recipesharingapp.model.response.RecipeResponse;


public interface RecipeService {
    RecipeResponse save(RecipeRequest recipeRequest);

    RecipeResponse update(RecipeUpdateRequest recipeUpdateRequest,Integer id);

    RecipeResponse getById(Integer id);

    DeleteResponse delete(Integer id);

    AllRecipeResponsePage getAll(RecipeQuery recipeQuery);

    String getRoleNameAndValidate();


}
