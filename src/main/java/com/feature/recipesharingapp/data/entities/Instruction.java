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
@Table(name = "instructions")
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instruction_id")
    private Integer instructionId;

    @Column(name = "step_number")
    private int stepNumber;

    @Column(name = "instruction_text", columnDefinition = "TEXT")
    private String instructionText;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeDetails recipe;

}

