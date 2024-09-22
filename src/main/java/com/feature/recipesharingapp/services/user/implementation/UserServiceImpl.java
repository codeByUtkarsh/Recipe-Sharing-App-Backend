package com.feature.recipesharingapp.services.user.implementation;

import com.feature.recipesharingapp.data.entities.Users;
import com.feature.recipesharingapp.data.repos.UserRepository;
import com.feature.recipesharingapp.mapper.UserMapper;
import com.feature.recipesharingapp.model.response.UserResponse;
import com.feature.recipesharingapp.services.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;




@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final Environment env;


    @Override
    public UserResponse getUserId(String userId) {
        UserResponse userResponse = null;
        PageRequest pageRequest = PageRequest.of(0, 1);
        Users users = userRepository.findByIdAndStatus(userId, "ACTIVE", pageRequest);
        if (users != null) {
            userResponse = UserMapper.mapToUserResponse(users);
        }
        return userResponse;
    }

}
