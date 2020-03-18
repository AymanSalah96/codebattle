package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
}
