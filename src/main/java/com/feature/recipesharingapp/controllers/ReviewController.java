package com.feature.recipesharingapp.controllers;

import com.feature.recipesharingapp.model.request.*;
import com.feature.recipesharingapp.model.response.*;
import com.feature.recipesharingapp.services.review.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling review-related operations.
 * Provides endpoints for adding, updating, retrieving, and deleting reviews.
 */
@Validated
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Adds a new review for a specific recipe.
     *
     * @param addReviewRequest the request object containing review details.
     * @param id the ID of the recipe for which the review is being added.
     * @return ResponseEntity<ReviewResponse> containing the details of the added review.
     */
    @PostMapping("/{id}")
    public ResponseEntity<ReviewResponse> save(@Valid @RequestBody ReviewRequest addReviewRequest, @PathVariable("id") Integer id) {
        log.info("Received request to add a new review for recipe with ID: {}", id);
        ReviewResponse addReviewResponse = reviewService.save(addReviewRequest, id);
        log.info("New review added successfully for recipe with ID: {}", id);
        return new ResponseEntity<>(addReviewResponse, HttpStatus.CREATED);
    }



    /**
     * Retrieves all reviews for a specific recipe by its ID.
     *
     * @param id the ID of the recipe for which reviews are being retrieved.
     * @return ResponseEntity<List<ReviewResponse>> containing the list of reviews for the specified recipe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<ReviewResponse>> getByRecipeId(@PathVariable("id") Integer id) {
        log.info("Received request to retrieve reviews for recipe with ID: {}", id);
        List<ReviewResponse> reviewResponse = reviewService.getByRecipeId(id);
        log.info("Retrieved reviews for recipe with ID: {} successfully.", id);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id the ID of the review to be deleted.
     * @return ResponseEntity<DeleteResponse> containing the result of the delete operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable("id") Integer id) {
        log.info("Received request to delete review with ID: {}", id);
        DeleteResponse deleteResponse = reviewService.delete(id);
        log.info("Review with ID {} deleted successfully.", id);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    /**
     * Updates an existing review.
     *
     * @param reviewUpdateRequest the request object containing updated review details.
     * @param id the ID of the review to be updated.
     * @return ResponseEntity<ReviewResponse> containing the updated review details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest, @PathVariable("id") Integer id) {
        log.info("Received request to update review with ID: {}", id);
        ReviewResponse updateReviewResponse = reviewService.update(reviewUpdateRequest, id);
        log.info("Review with ID {} updated successfully.", id);
        return new ResponseEntity<>(updateReviewResponse, HttpStatus.OK);
    }
}
