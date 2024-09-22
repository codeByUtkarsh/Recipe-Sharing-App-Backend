package com.feature.recipesharingapp.data.repos;

import com.feature.recipesharingapp.data.entities.AccessTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccesstokenRepository extends JpaRepository<AccessTokens,String> {
    Optional<AccessTokens> findByUserId(String userId);

    AccessTokens findByUserIdAndIsActive(String userId, Boolean isActive);

    AccessTokens findByTokenAndIsActive(String token, Boolean isActive);
}
