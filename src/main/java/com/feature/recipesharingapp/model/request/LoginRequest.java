package com.feature.recipesharingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotNull(message = "Username cannot be null.")
    @NotBlank(message = "Username cannot be blank.")
    @Pattern(
            regexp = "^(\\w[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,5})$",
            message = "Please Enter Valid  Email Accept @ and dot other Special characters and space are not allowed")
    private String username;

    @NotNull(message = "Password cannot be null.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;
}

