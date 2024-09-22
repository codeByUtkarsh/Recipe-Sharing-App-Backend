package com.feature.recipesharingapp.model.response;


import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class InstructionResponse {
    private Integer instructionId;
    private Integer stepNumber;
    private String instructionText;
}
