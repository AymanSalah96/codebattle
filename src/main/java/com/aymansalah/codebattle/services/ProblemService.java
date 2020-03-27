package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.repositories.ProblemRepository;
import com.aymansalah.codebattle.util.judge.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Problem createdProblem = this.problemRepository.saveAndFlush(problem);
        try {
            fileUploadService.saveIOFilesForProblemId(createdProblem.getId(), ioFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Problem> getProblemsCreatedByUsername(String username) {
        return problemRepository.findByCreatorUsername(username);
    }

    public Problem getProblemById(long problemId) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if(null != problem) {
            problem.setIo(fileUploadService.getIOFileForProblemId(problem.getId()));
            problem.setSamples(fileUploadService.getSamplesForProblemId(problemId));
        }
        return problem;
    }

    public Path getMainInputTestFile(Problem problem) {
        for(File file : problem.getIo()) {
            if(!Helper.fileNameStartsWith(file.getName(), "sample") && Helper.isInputFile(file.getName()))
                return file.toPath();
        }
        return null;
    }

    public void editProblem(Problem problem, MultipartFile[] ioFiles) {
        Problem createdProblem = this.problemRepository.saveAndFlush(problem);
        if(ioFiles.length > 0 && !ioFiles[0].getOriginalFilename().isEmpty()) {
            fileUploadService.updateProblemFiles(createdProblem.getId(), ioFiles);
        }
    }
}
