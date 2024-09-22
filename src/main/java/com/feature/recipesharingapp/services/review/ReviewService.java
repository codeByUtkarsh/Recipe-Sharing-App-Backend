package com.feature.recipesharingapp.services.review;
import com.feature.recipesharingapp.model.request.ReviewRequest;
import com.feature.recipesharingapp.model.request.ReviewUpdateRequest;
import com.feature.recipesharingapp.model.response.DeleteResponse;
import com.feature.recipesharingapp.model.response.ReviewResponse;

import java.util.List;


public interface ReviewService {
    ReviewResponse save(ReviewRequest reviewRequest,Integer id);

    ReviewResponse update(ReviewUpdateRequest reviewUpdateRequest,Integer id);


    List<ReviewResponse> getByRecipeId(Integer id);

    DeleteResponse delete(Integer id);
}
