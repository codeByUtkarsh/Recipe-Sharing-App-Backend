package com.feature.recipesharingapp.model.response;



import lombok.Builder;
import lombok.Data;
import java.util.List;


@Data
@Builder
public class AllReviewResponsePage {
    private List<ReviewResponse> allRecipeResponseList;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalCount;
    private Integer totalPages;
}

