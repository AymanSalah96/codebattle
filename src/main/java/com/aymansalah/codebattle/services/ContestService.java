package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.*;
import com.aymansalah.codebattle.repositories.ContestParticipantsRepository;
import com.aymansalah.codebattle.repositories.ContestProblemsRepository;
import com.aymansalah.codebattle.repositories.ContestRepository;
import com.aymansalah.codebattle.util.judge.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class ContestService {

    @Autowired
    private ContestParticipantsRepository contestParticipantsRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestProblemsRepository contestProblemsRepository;

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public List<Contest> getContestsCreatedByUsername(String username) {
        return contestRepository.findByCreatorUsername(username);
    }

    public void createNewContest(Contest contest) {
        String authUsername = userService.getAuthenticatedUsername();
        if(null == authUsername)
            return;
        contest.setCreatorUsername(authUsername);
        contestRepository.saveAndFlush(contest);
    }

    public Contest getById(long contestId) {
        Contest contest = contestRepository.findById(contestId).orElse(null);
        if(null == contest)
            return null;
        List<ContestProblems> cps = contestProblemsRepository.findByContestId(contestId);
        HashMap<Long, String> mapIdxToString = new HashMap<>();
        for(ContestProblems p : cps)
            mapIdxToString.put(p.getProblemId(), p.idxToString());
        for(Problem p : contest.getProblems())
            p.setIndex(mapIdxToString.get(p.getId()));
        return contest;
    }

    public void addProblemToContest(long contestId, long problemId) {
        List<ContestProblems> contestProblems = getContestProblems(contestId);
        Collections.sort(contestProblems);
        ContestProblems cp = new ContestProblems();
        cp.setContestId(contestId);
        cp.setProblemId(problemId);
        cp.setProblemIdx(contestProblems.size() + 1);
        contestProblemsRepository.saveAndFlush(cp);
    }

    public void updateContestWithExisting(Contest updatedContest, Contest existingContest) {
        Contest newContest = (Contest) Helper.replaceNotNullProperties(existingContest, updatedContest);
        contestRepository.saveAndFlush(newContest);
    }

    public List<ContestProblems> getContestProblems(long contestId) {
        return contestProblemsRepository.findByContestId(contestId);
    }

    public void registerUser(User user, Contest contest) {
        ContestParticipants participant = contestParticipantsRepository.findByParticipantUsernameAndContestId(user.getUsername(), contest.getId());
        if(null == participant) {
            ContestParticipants newParticipant = new ContestParticipants();
            newParticipant.setContestId(contest.getId());
            newParticipant.setParticipantUsername(user.getUsername());
            contestParticipantsRepository.saveAndFlush(newParticipant);
        }
    }

    public boolean isRegisteredInContest(long contestId, String username) {
        return null != contestParticipantsRepository.findByParticipantUsernameAndContestId(username, contestId);
    }

    public Problem getProblemIndexInContest(long contestId, String problemIndex) {
        ContestProblems contestProblem = contestProblemsRepository.findByContestIdAndProblemIdx(contestId, toInteger(problemIndex));
        if(null == contestProblem)
            return null;
        return problemService.getProblemById(contestProblem.getProblemId());
    }

    private int toInteger(String A) {
        int result = 0;
        for(int i = A.length() - 1, j = 0; i >= 0; i--) {
            result += (A.charAt(i) - 'A' + 1) * (int)Math.pow(26, j++);
        }
        return result;
    }

}
