package com.feature.recipesharingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstructionRequest {

    @NotNull(message = "Instruction cannot be null.")
    private Integer instructionId;

    @NotNull(message = "Step number is mandatory")
    private Integer stepNumber;

    @NotBlank(message = "Instruction text is mandatory")
    @Size(max = 2000, message = "Instruction text cannot exceed 2000 characters")
    private String instructionText;
}
