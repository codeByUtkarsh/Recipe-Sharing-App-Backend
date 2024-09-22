package com.feature.recipesharingapp.data.repos;

import com.feature.recipesharingapp.data.entities.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction,Long> {
}
