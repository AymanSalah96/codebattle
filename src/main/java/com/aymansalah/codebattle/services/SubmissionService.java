package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Submission;
import com.aymansalah.codebattle.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public List<Submission> getSubmissionsByUsernameForProblemId(String username, long problemId) {
        return submissionRepository.findByAuthorUsernameAndProblemId(username, problemId);
    }

    public boolean submit(long problemId, String username, MultipartFile file) {
        return fileUploadService.saveSubmittedOutput(problemId, username, file);
    }
}
