package com.feature.recipesharingapp.services.review.implementation;

import com.feature.recipesharingapp.config.JwtHelper;
import com.feature.recipesharingapp.data.entities.RecipeDetails;
import com.feature.recipesharingapp.data.entities.ReviewDetails;
import com.feature.recipesharingapp.data.repos.RecipeRepository;
import com.feature.recipesharingapp.data.repos.ReviewRepository;
import com.feature.recipesharingapp.data.specification.Review;
import com.feature.recipesharingapp.data.specification.Sorting;
import com.feature.recipesharingapp.exception.ApplicationErrorCode;
import com.feature.recipesharingapp.exception.CustomException;
import com.feature.recipesharingapp.mapper.ReviewMapper;
import com.feature.recipesharingapp.model.request.ReviewQuery;
import com.feature.recipesharingapp.model.request.ReviewRequest;
import com.feature.recipesharingapp.model.request.ReviewUpdateRequest;
import com.feature.recipesharingapp.model.response.AllReviewResponsePage;
import com.feature.recipesharingapp.model.response.DeleteResponse;
import com.feature.recipesharingapp.model.response.ReviewResponse;
import com.feature.recipesharingapp.model.response.UserResponse;
import com.feature.recipesharingapp.services.recipe.RecipeService;
import com.feature.recipesharingapp.services.review.ReviewService;
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
 * Implementation of ReviewService for managing review operations.
 * Provides methods for saving, updating, retrieving, deleting reviews, and more.
 */
@Service
@Slf4j
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final UserService userService;
    private final JwtHelper helper;

    /**
     * Saves a new review for a specific recipe.
     *
     * @param reviewRequest the request object containing details of the review to be saved.
     * @param id the ID of the recipe to which the review belongs.
     * @return ReviewResponse containing the details of the saved review.
     */
    @Override
    public ReviewResponse save(ReviewRequest reviewRequest, Integer id) {
        log.info("Saving new Review for Recipe ID: {}", id);

        String userId = helper.getUserId();
        UserResponse userResponse = userService.getUserId(userId);
        RecipeDetails recipeDetails = recipeRepository.findByRecipeIdAndIsDeleted(id, false);
        if (recipeDetails == null) {
            log.error("Recipe not found with ID: {}", id);
            throw new CustomException(ApplicationErrorCode.RECIPE_NOT_FOUND);
        }

        ReviewDetails reviewDetails = ReviewMapper.mapReviewRequestToReviewDetails(reviewRequest, id, userResponse);
        reviewRepository.save(reviewDetails);
        log.info("Review with ID {} successfully saved.", reviewDetails.getReviewId());
        return ReviewMapper.mapReviewDetailsToReviewResponse(reviewDetails);
    }

    /**
     * Updates an existing review based on the provided request and review ID.
     *
     * @param reviewUpdateRequest the request object containing updated details of the review.
     * @param id the ID of the review to be updated.
     * @return ReviewResponse containing the details of the updated review.
     */
    @Override
    public ReviewResponse update(ReviewUpdateRequest reviewUpdateRequest, Integer id) {
        log.info("Updating Review with ID: {}", id);

        ReviewDetails reviewDetails = reviewRepository.findByReviewIdAndIsDeleted(id, false);
        if (reviewDetails == null) {
            log.error("Review not found with ID: {}", id);
            throw new CustomException(ApplicationErrorCode.REVIEW_NOT_FOUND);
        }

        reviewDetails = ReviewMapper.mapReviewUpdateRequestToReviewDetails(reviewUpdateRequest, reviewDetails);
        reviewDetails = reviewRepository.save(reviewDetails);
        log.info("Review with ID {} successfully updated.", id);
        return ReviewMapper.mapReviewDetailsToReviewResponse(reviewDetails);
    }

    /**
     * Retrieves a list of reviews for a specific recipe.
     *
     * @param id the ID of the recipe whose reviews are to be retrieved.
     * @return a list of ReviewResponse containing details of the reviews for the specified recipe.
     */
    @Override
    public List<ReviewResponse> getByRecipeId(Integer id) {
        log.info("Getting Reviews for Recipe ID: {}", id);

        List<ReviewDetails> reviewDetailsList = reviewRepository.findAllByRecipeIdAndIsDeleted(id, false);
        if (reviewDetailsList == null || reviewDetailsList.isEmpty()) {
            log.error("No reviews found for Recipe ID: {}", id);
            throw new CustomException(ApplicationErrorCode.REVIEW_NOT_FOUND);
        }

        return mapReviewDetailsListToReviewResponseList(reviewDetailsList);
    }

    /**
     * Maps a list of ReviewDetails to a list of ReviewResponse.
     *
     * @param reviewDetailsList the list of ReviewDetails to be mapped.
     * @return a list of ReviewResponse corresponding to the provided ReviewDetails.
     */
    public static List<ReviewResponse> mapReviewDetailsListToReviewResponseList(List<ReviewDetails> reviewDetailsList) {
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (ReviewDetails reviewDetails : reviewDetailsList) {
            reviewResponses.add(ReviewMapper.mapReviewDetailsToReviewResponse(reviewDetails));
        }
        return reviewResponses;
    }

    /**
     * Deletes a review by setting its isDeleted flag to true.
     *
     * @param id the ID of the review to be deleted.
     * @return DeleteResponse indicating the result of the delete operation.
     */
    @Override
    public DeleteResponse delete(Integer id) {
        log.info("Deleting Review with ID: {}", id);

        ReviewDetails reviewDetails = reviewRepository.findByReviewIdAndIsDeleted(id, false);
        if (reviewDetails == null) {
            log.error("Review not found with ID: {}", id);
            throw new CustomException(ApplicationErrorCode.REVIEW_NOT_FOUND);
        }

        reviewDetails.setIsDeleted(true);
        reviewRepository.save(reviewDetails);
        log.info("Review with ID {} successfully deleted.", id);
        return new DeleteResponse("Review is successfully deleted from the database");
    }


    /**
     * Validates the page number and page size for pagination.
     *
     * @param reviewQuery the query parameters containing page number and page size.
     */
    public void validatePageNumberAndSize(ReviewQuery reviewQuery) {
        log.info("Validating page number and page size. PageNumber: {}, PageSize: {}", reviewQuery.getPageNumber(), reviewQuery.getPageSize());

        if (reviewQuery.getPageNumber() == null || reviewQuery.getPageNumber() < 0) {
            log.error("Invalid page number: {}", reviewQuery.getPageNumber());
            throw new CustomException(ApplicationErrorCode.INVALID_PAGE_NUMBER);
        }

        if (reviewQuery.getPageSize() == null || reviewQuery.getPageSize() <= 0) {
            log.warn("Invalid page size: {}. Setting to default value 5.", reviewQuery.getPageSize());
            reviewQuery.setPageSize(5);
        }
    }
}
