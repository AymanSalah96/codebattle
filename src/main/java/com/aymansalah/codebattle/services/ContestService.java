package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.Contest;
import com.aymansalah.codebattle.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }
}
