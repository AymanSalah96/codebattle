package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserService userService;

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public void createNewProblem(Problem problem, MultipartFile[] ioFiles) {
        String authUsername = userService.getAuthenticatedUsername();
        if(null == authUsername)
            return;
        problem.setCreatorUsername(authUsername);
        // Temp contest
        problem.setContestId(5000);
        Problem createdProblem = this.problemRepository.saveAndFlush(problem);
        try {
            fileUploadService.saveIOFilesForProblemId(createdProblem.getId(), ioFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
