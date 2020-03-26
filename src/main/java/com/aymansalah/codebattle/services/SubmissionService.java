package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Contest;
import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.models.Submission;
import com.aymansalah.codebattle.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public List<Submission> getSubmissionsByUsernameForProblemId(String username, String problemIndex) {
        return submissionRepository.findByAuthorUsernameAndProblemIndex(username, problemIndex);
    }

    public boolean submit(Contest contest, Problem problem, String username, MultipartFile file, boolean saveSubmissionToDb) {
        boolean result = fileUploadService.saveSubmittedOutput(contest, problem, username, file);
        if(saveSubmissionToDb) {
            Submission submission = new Submission();
            submission.setVerdict(result ? "OK" : "WRONG");
            submission.setAuthorUsername(username);
            submission.setProblemIndex(problem.getIndex());
            submission.setContestId(contest.getId());
            submission.setSubmissionTime(LocalDateTime.now());
            submissionRepository.saveAndFlush(submission);
        }
        return result;
    }

    public int getTriesCountForProblem(long contestID, String problemIndex, String username) {
        return submissionRepository.countByAuthorUsernameAndProblemIndexAndContestId(username, problemIndex, contestID);
    }
}
