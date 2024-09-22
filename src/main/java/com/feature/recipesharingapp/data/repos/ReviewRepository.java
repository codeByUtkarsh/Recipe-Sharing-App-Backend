package com.feature.recipesharingapp.data.repos;

import com.feature.recipesharingapp.data.entities.ReviewDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewDetails,Integer> {


    ReviewDetails findByReviewIdAndIsDeleted(Integer id, boolean isDeleted);

    List<ReviewDetails> findAllByRecipeIdAndIsDeleted(Integer recipeId, boolean isDeleted);  // Fetch all reviews for a recipe


    Page<ReviewDetails> findAll(Specification<ReviewDetails> specification, Pageable pageable);




}
