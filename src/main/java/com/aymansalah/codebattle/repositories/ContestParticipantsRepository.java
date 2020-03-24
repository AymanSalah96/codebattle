package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.ContestParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestParticipantsRepository extends JpaRepository<ContestParticipants, Long> {
    List<ContestParticipants> findByParticipantUsername(String username);
    List<ContestParticipants> findByContestId(long contestId);
    ContestParticipants findByParticipantUsernameAndContestId(String username, long contestId);
}
