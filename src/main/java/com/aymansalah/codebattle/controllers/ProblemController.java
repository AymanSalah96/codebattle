package com.aymansalah.codebattle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProblemController {

    @GetMapping("/problems")
    public String getProblemsPage() {
        return "problem/index";
    }
}
