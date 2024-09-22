package com.feature.recipesharingapp.services.auth;

import com.feature.recipesharingapp.model.request.LoginRequest;
import com.feature.recipesharingapp.model.response.LoginResponse;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException;

    void logout();
}
