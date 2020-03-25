package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAuthorUsernameAndProblemId(String username, long problemId);
    int countByAuthorUsernameAndProblemIdAndContestId(String username, long problemId, long contestId);
}
