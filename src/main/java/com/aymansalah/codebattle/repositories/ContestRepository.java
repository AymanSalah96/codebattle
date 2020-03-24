package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByCreatorUsername(String username);
}
