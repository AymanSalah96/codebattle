package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.ContestProblems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestProblemsRepository extends JpaRepository<ContestProblems, Long> {
    List<ContestProblems> findByContestId(long contestId);
    ContestProblems findByContestIdAndProblemIdx(long contestId, int problemIdx);
}
