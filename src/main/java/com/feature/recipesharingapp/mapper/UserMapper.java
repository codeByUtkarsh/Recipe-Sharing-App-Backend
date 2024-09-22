package com.feature.recipesharingapp.mapper;

import com.feature.recipesharingapp.data.entities.Users;
import com.feature.recipesharingapp.model.response.UserResponse;

public class UserMapper {
    public static UserResponse mapToUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
