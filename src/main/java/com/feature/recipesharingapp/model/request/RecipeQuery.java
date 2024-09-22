package com.feature.recipesharingapp.model.request;


import lombok.Data;

@Data
public class RecipeQuery {

    private String type;
    private String sortBy;
    private String title;
    private String ingredient;
    private String sortOrder = "asc";
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}
