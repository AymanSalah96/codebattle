package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Problem saveProblem(Problem problem) {
        return problemRepository.saveAndFlush(problem);
    }
}
