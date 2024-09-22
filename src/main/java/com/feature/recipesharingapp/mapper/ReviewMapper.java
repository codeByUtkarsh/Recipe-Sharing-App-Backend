package com.feature.recipesharingapp.mapper;

import com.feature.recipesharingapp.data.entities.ReviewDetails;
import com.feature.recipesharingapp.model.request.ReviewRequest;
import com.feature.recipesharingapp.model.request.ReviewUpdateRequest;
import com.feature.recipesharingapp.model.response.AllReviewResponsePage;
import com.feature.recipesharingapp.model.response.ReviewResponse;
import com.feature.recipesharingapp.model.response.UserResponse;

import java.util.Date;
import java.util.List;

public class ReviewMapper {
    public static ReviewDetails mapReviewRequestToReviewDetails(ReviewRequest reviewRequest, Integer recipeId, UserResponse userResponse){
        return ReviewDetails.builder()
                .recipeId(recipeId)
                .userId(userResponse.getId())
                .reviewerName(userResponse.getName())
                .reviewText(reviewRequest.getReviewText())
                .reviewDate(new Date())
                .rating(reviewRequest.getRating())
                .isDeleted(false)
                .modifiedAt(new Date())
                .modifiedBy(userResponse.getRole())
                .build();
    }



    public static ReviewResponse mapReviewDetailsToReviewResponse(ReviewDetails reviewDetails){
        return ReviewResponse.builder()
                .reviewId(reviewDetails.getReviewId())
                .userId(reviewDetails.getUserId())
                .reviewerName(reviewDetails.getReviewerName())
                .comment(reviewDetails.getReviewText())
                .rating(reviewDetails.getRating())
                .build();
    }

    public static AllReviewResponsePage mapReviewResponseToAllReviewResponsePage(List<ReviewResponse> reviewResponses, Integer pageNumber, Integer pageSize, Long totalCount, Integer totalPages){
        return AllReviewResponsePage.builder()
                .allRecipeResponseList(reviewResponses)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .build();
    }




    public static ReviewDetails mapReviewUpdateRequestToReviewDetails(ReviewUpdateRequest reviewUpdateRequest,ReviewDetails reviewDetails){
        return ReviewDetails.builder()
                .reviewId(reviewDetails.getReviewId())
                .recipeId(reviewDetails.getRecipeId())
                .userId(reviewDetails.getUserId())
                .createdBy(reviewDetails.getCreatedBy())
                .isDeleted(false)
                .reviewerName(reviewDetails.getReviewerName())
                .reviewDate(reviewDetails.getReviewDate())
                .modifiedBy(reviewDetails.getModifiedBy())
                .reviewText(reviewUpdateRequest.getReviewText())
                .rating(reviewUpdateRequest.getRating())
                .modifiedAt(new Date())
                .build();
    }
}


