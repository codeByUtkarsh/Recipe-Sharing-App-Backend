package com.feature.recipesharingapp.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer ingredientId;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "quantity")
    private String quantity;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeDetails recipe;

}

