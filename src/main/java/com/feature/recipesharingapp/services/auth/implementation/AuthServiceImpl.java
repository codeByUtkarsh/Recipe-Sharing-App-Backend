package com.feature.recipesharingapp.services.auth.implementation;

import com.feature.recipesharingapp.config.JwtHelper;
import com.feature.recipesharingapp.data.entities.AccessTokens;
import com.feature.recipesharingapp.data.entities.Users;
import com.feature.recipesharingapp.data.repos.AccesstokenRepository;
import com.feature.recipesharingapp.data.repos.UserRepository;
import com.feature.recipesharingapp.exception.ApplicationErrorCode;
import com.feature.recipesharingapp.exception.CustomException;
import com.feature.recipesharingapp.model.request.LoginRequest;
import com.feature.recipesharingapp.model.response.LoginResponse;
import com.feature.recipesharingapp.services.auth.AuthService;
import com.feature.recipesharingapp.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtils;

    private final UserRepository userRepository;

    private final AccesstokenRepository accesstokenRepository;

    private final JwtHelper jwtHelper;



    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Attempting login for username: {}", loginRequest.getUsername());

        Users userDetails = userRepository.findByEmail(loginRequest.getUsername());

        if (userDetails == null) {
            log.error("Username {} is not valid", loginRequest.getUsername());
            throw new CustomException(ApplicationErrorCode.INVALID_USERNAME);
        }
        LoginResponse loginResponse = null;

        if (!userDetails.getEmail().isEmpty()) {

            String password = loginRequest.getPassword();
            if (password.equals(userDetails.getPassword())) {

                loginResponse = handleLoginToken(userDetails.getId());
            } else {
                log.error("Password not valid for user with ID: {}", userDetails.getId());
                throw new CustomException(ApplicationErrorCode.INVALID_PASSWORD);
            }
        }
        log.info("Login response generated successfully");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }




    /**
     * Handles the generation of a login token for the given user.
     *
     * @param userId User ID for whom the token is generated.
     * @return LoginResponse containing the generated token and reference code.
     */
    private LoginResponse handleLoginToken(String userId) {
        log.info("Handling login token for user with ID: {}", userId);
        String generatedToken = generateToken(userId);
        Date expireIn = jwtTokenUtils.getExpirationDateFromToken(generatedToken);
        saveToken(userId, generatedToken, expireIn, true);
        LoginResponse loginResponse =
                LoginResponse.builder().token(generatedToken).userId(userId).build();
        log.info("Login token handled successfully");
        return loginResponse;

    }




    /**
     * Generates a random reference code consisting of two parts separated by a hyphen.
     *
     * @return The generated random reference code.
     */
    public String randomRefCode() {
        String rand1 = RandomStringUtils.random(3, true, false).toLowerCase();
        String rand2 = RandomStringUtils.random(3, true, false).toLowerCase();
        return rand1 + "-" + rand2;
    }

    /**
     * Generates a new authentication token for the given user ID.
     *
     * @param userId User ID for whom the token is generated.
     * @return The generated authentication token.
     */
    private String generateToken(String userId) {
        Optional<Users> roleName = userRepository.findById(userId);

        String role = roleName.get().getRole();
        return jwtTokenUtils.generateToken(userId, role);
    }

    /**
     * Saves or updates an AccessTokens entity based on the provided user ID.
     *
     * @param userId   The user ID associated with the AccessTokens.
     * @param token    The access token to be saved or updated.
     * @param expireIn The expiration date of the access token.
     * @param isActive Flag indicating whether the access token is active.
     */
    private void saveToken(String userId, String token, Date expireIn, Boolean isActive) {
        AccessTokens accessTokens = AccessTokens.builder()
                .token(token)
                .userId(userId)
                .expiry(expireIn)
                .isActive(isActive)
                .build();

        Optional<AccessTokens> existingToken = accesstokenRepository.findByUserId(userId);

        existingToken.ifPresentOrElse(
                exiting -> {
                    exiting.setToken(accessTokens.getToken());
                    exiting.setIsActive(accessTokens.getIsActive());
                    exiting.setExpiry(accessTokens.getExpiry());
                    accesstokenRepository.save(exiting);
                },
                () -> accesstokenRepository.save(accessTokens));
    }


    /**
     * Logs out the user by invalidating active access tokens associated with their ID.
     * Throws a RuntimeException if no active tokens are found for the user.
     */
    @Override
    public void logout() {
        String userId = jwtHelper.getUserId();
        log.info("Logging out user with ID: {}", userId);

        AccessTokens accessTokens = accesstokenRepository.findByUserIdAndIsActive(userId, true);

        if (accessTokens == null) {
            log.warn("No active tokens found for user with ID: {}", userId);
            throw new CustomException(ApplicationErrorCode.TOKEN_NOT_FOUND);
        }

        log.info("Logging out user and invalidating {} token(s).", accessTokens.getToken());
        accessTokens.setIsActive(false);
        accesstokenRepository.save(accessTokens);

        log.info("Logout successful for user with ID: {}", userId);
    }
}
