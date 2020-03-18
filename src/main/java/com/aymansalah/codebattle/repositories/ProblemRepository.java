package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
