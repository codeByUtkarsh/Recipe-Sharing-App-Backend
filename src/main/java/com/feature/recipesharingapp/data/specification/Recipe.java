package com.feature.recipesharingapp.data.specification;

import com.feature.recipesharingapp.data.entities.Ingredient;
import com.feature.recipesharingapp.data.entities.RecipeDetails;
import com.feature.recipesharingapp.model.request.RecipeQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Recipe {



    public static Specification<RecipeDetails> searchRecipe(RecipeQuery recipeQuery) {
        log.info("Building search specification with criteria - Title : {}, Type :{}," , recipeQuery.getTitle(),recipeQuery.getType());
        Specification<RecipeDetails> spec = Specification.where(null);

        spec = spec.and(recipeIsPresent());

        spec = (recipeQuery.getType() != null && !recipeQuery.getType().isEmpty()) ? spec.and(createFilter("type", recipeQuery.getType())) : spec;
        spec = (recipeQuery.getTitle() != null && !recipeQuery.getTitle().isEmpty()) ? spec.and(createFilter("title", recipeQuery.getTitle())) : spec;
        spec = (recipeQuery.getIngredient()!=null && !recipeQuery.getIngredient().isEmpty())? spec.and(filterByIngredient(recipeQuery.getIngredient())):spec;

        log.info("Built specification successfully.");
        return spec;
    }



    private static Specification<RecipeDetails> createFilter(String attribute, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(attribute)),
                    "%" + value.toLowerCase() + "%"
            );
    }


    private static Specification<RecipeDetails> filterByIngredient(String ingredientName) {
        return (root, query, criteriaBuilder) -> {
            Join<RecipeDetails, Ingredient> ingredientJoin = root.join("ingredients", JoinType.INNER);

            return criteriaBuilder.like(ingredientJoin.get("ingredientName"), "%" + ingredientName.trim().replace(" ", "%") + "%");
        };
    }

    private static Specification<RecipeDetails> recipeIsPresent() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }


}




