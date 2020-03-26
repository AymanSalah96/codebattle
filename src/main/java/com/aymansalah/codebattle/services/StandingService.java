package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Contest;
import com.aymansalah.codebattle.models.Submission;
import com.aymansalah.codebattle.repositories.SubmissionRepository;
import com.aymansalah.codebattle.util.RankRecord;
import com.aymansalah.codebattle.util.SubmissionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StandingService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ContestService contestService;

    public List<RankRecord> getContestStanding(long contestId) {
        List<RankRecord> rankList = new ArrayList<>();
        Contest contest = contestService.getById(contestId);
        if(null == contest)
            return rankList;
        int contestTimeInSeconds = getSeconds(contest.getStartDate());
        List<Submission> submissions = submissionRepository.findByContestId(contestId);
        HashMap<String, RankRecord> participantsSubmissionsMap = new HashMap<>();

        for(Submission submission : submissions) {
            RankRecord rankRecord = participantsSubmissionsMap
                    .getOrDefault(submission.getAuthorUsername(), new RankRecord(submission.getAuthorUsername()));


            SubmissionSummary summary = rankRecord
                    .getProblemSummaryMap().getOrDefault(submission.getProblemIndex(), new SubmissionSummary());
            summary.setProblemIndex(submission.getProblemIndex());

            String verdict = submission.getVerdict();

            int submissionTimeInSeconds = getSeconds(submission.getSubmissionTime()) - contestTimeInSeconds;


            if(summary.getVerdict().equalsIgnoreCase("NOT_DETERMINED")) {
                summary.setVerdict(verdict);
                if (verdict.equalsIgnoreCase("OK")) {
                    summary.setAcTimeInSeconds(submissionTimeInSeconds);
                }
                summary.setTriesCount(summary.getTriesCount() + 1);
            } else {
                if(summary.getVerdict().equalsIgnoreCase("OK")) {
                    if(verdict.equalsIgnoreCase("OK")) {
                        summary.setAcTimeInSeconds(Math.min(summary.getAcTimeInSeconds(), submissionTimeInSeconds));
                    } else {
                        if(submissionTimeInSeconds < summary.getAcTimeInSeconds()) {
                            summary.setTriesCount(summary.getTriesCount() + 1);
                        }
                    }
                } else { // WRONG before
                    if(verdict.equalsIgnoreCase("OK")) {
                        summary.setVerdict(verdict);
                        summary.setAcTimeInSeconds(submissionTimeInSeconds);
                    }
                    summary.setTriesCount(summary.getTriesCount() + 1);
                }

            }
            rankRecord.getProblemSummaryMap().put(submission.getProblemIndex(), summary);
            participantsSubmissionsMap.put(submission.getAuthorUsername(), rankRecord);
        }

        for(Map.Entry rankEntry : participantsSubmissionsMap.entrySet()) {
            RankRecord rankRecord = (RankRecord) rankEntry.getValue();
            int solved = 0, penalty = 0;
            for(Map.Entry summaryEntry : rankRecord.getProblemSummaryMap().entrySet()) {
                String problemIdx = (String) summaryEntry.getKey();
                SubmissionSummary summary = (SubmissionSummary) summaryEntry.getValue();
                if(summary.getVerdict().equalsIgnoreCase("OK")) {
                    penalty += summary.getAcTimeInSeconds() / 60;
                    if(summary.getTriesCount() > 1)
                        penalty += 20; // 20 minutes penalty
                    solved++;
                }
            }
            rankRecord.setPenalty(penalty);
            rankRecord.setSolved(solved);
            rankList.add(rankRecord);
        }

        Collections.sort(rankList);
        if(!rankList.isEmpty()) {
            rankList.get(0).setRank(1);
            int nxtRank = 2;
            for (int i = 1; i < rankList.size(); i++) {
                if (rankList.get(i).getSolved().equals(rankList.get(i - 1).getSolved())
                && rankList.get(i).getPenalty().equals(rankList.get(i - 1).getPenalty()))
                    rankList.get(i).setRank(rankList.get(i - 1).getRank());
                else
                    rankList.get(i).setRank(nxtRank++);
            }
        }
        return rankList;
    }

    private int getSeconds(LocalDateTime time) {
        return time.getSecond() + time.getMinute() * 60 + time.getHour() * 60 * 60;
    }
}
