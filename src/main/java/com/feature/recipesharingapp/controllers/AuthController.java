package com.feature.recipesharingapp.controllers;

import com.feature.recipesharingapp.model.request.LoginRequest;
import com.feature.recipesharingapp.model.response.LoginResponse;
import com.feature.recipesharingapp.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Controller for handling authentication-related endpoints.
 * This includes login and logout operations.
 */
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Handles login requests by delegating to the authService.
     *
     * @param loginRequest the request object containing login credentials.
     * @return ResponseEntity<LoginResponse> containing the result of the login attempt.
     * @throws NoSuchAlgorithmException if the algorithm used for hashing is not available.
     * @throws InvalidKeySpecException if the key specification for the password is invalid.
     */
    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Received login request for username: {}", loginRequest.getUsername());
        try {
            ResponseEntity<LoginResponse> response = authService.login(loginRequest);
            log.info("Login successful for username: {}", loginRequest.getUsername());
            return response;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Login failed for username: {} due to {}", loginRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    /**
     * Handles logout requests by delegating to the authService.
     * Logs out the currently authenticated user.
     */
    @PostMapping("/logout")
    public void logout() {
        log.info("Received logout request");
        try {
            authService.logout();
            log.info("Logout successful");
        } catch (Exception e) {
            log.error("Logout failed due to {}", e.getMessage());

        }
    }
}
