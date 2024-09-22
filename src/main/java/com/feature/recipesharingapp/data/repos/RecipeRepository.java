package com.feature.recipesharingapp.data.repos;

import com.feature.recipesharingapp.data.entities.RecipeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository <RecipeDetails,Integer> {

    RecipeDetails findByRecipeIdAndIsDeleted(Integer id,Boolean isDeleted);


    Page<RecipeDetails> findAll(Specification<RecipeDetails> specification, Pageable pageable);

}
