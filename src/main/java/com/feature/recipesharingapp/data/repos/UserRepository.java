package com.feature.recipesharingapp.data.repos;
import com.feature.recipesharingapp.data.entities.Users;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
    Users findByEmail(String email);

    Optional<Users> findById(String id);

    Users findByIdAndStatus(String id, String status, PageRequest pageRequest);
}
