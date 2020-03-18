package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
