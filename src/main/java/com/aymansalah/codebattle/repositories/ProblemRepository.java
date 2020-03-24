package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByCreatorUsername(String username);
}
