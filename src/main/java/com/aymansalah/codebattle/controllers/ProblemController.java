package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.services.ContestService;
import com.aymansalah.codebattle.services.ProblemService;
import com.aymansalah.codebattle.services.UserService;
import com.aymansalah.codebattle.validators.ProblemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProblemController {

    @Autowired
    UserService userService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @GetMapping("/problems")
    public String getProblemsPage(Model model, RedirectAttributes redirectAttributes) {
        List<Problem> problemList = problemService.getAllProblems();
        model.addAttribute("problemsList", problemList);
        return "problem/index";
    }

    @GetMapping("/problems/new")
    @PreAuthorize("isAuthenticated()")
    public String getProblemCreationPage(Model model) {
        model.addAttribute("problem", new Problem());
        return "problem/new";
    }


    @PostMapping("/problems/new")
    @PreAuthorize("isAuthenticated()")
    public String postNewProblem(@RequestParam("ioFiles")MultipartFile[] ioFiles,
                                 @ModelAttribute("problem") Problem problem,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        ProblemValidator.validate(problem, result);
        if(result.hasErrors()) {
            return "problem/new";
        }
        return "redirect:/problems";
    }
}
