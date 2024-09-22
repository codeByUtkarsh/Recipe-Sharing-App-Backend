package com.feature.recipesharingapp.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_details")
public class ReviewDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "reviewer_name")
    private String reviewerName;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review_date")
    private Date reviewDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}

