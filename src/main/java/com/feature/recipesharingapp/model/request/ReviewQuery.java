package com.feature.recipesharingapp.model.request;

import lombok.Data;


@Data
public class ReviewQuery {
    private String review;
    private String sortOrder = "asc";
    private String sortBy="rating";
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}




