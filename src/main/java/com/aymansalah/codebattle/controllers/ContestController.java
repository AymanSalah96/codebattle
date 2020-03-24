package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.Contest;
import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.services.ContestService;
import com.aymansalah.codebattle.services.ProblemService;
import com.aymansalah.codebattle.services.UserService;
import com.aymansalah.codebattle.validators.ContestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder ( WebDataBinder binder ) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/contests")
    public String getContestsPage(Model model) {
        model.addAttribute("contestsList", contestService.getAllContests());
        return "contest/index";
    }

    @GetMapping("/contests/new")
    @PreAuthorize("isAuthenticated()")
    public String getContestCreationPage(Model model) {
        model.addAttribute("contest", new Contest());
        return "contest/new";
    }

    @GetMapping("/contests/creator/{username}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String getUserOwnContestsPage(@PathVariable("username") String username, Model model) {
        List<Contest> contestsList = contestService.getContestsCreatedByUsername(username);
        model.addAttribute("contestsList", contestsList);
        return "contest/creator";
    }

    @PostMapping("/contests/new")
    @PreAuthorize("isAuthenticated()")
    public String postNewContest(@ModelAttribute("contest") Contest contest, BindingResult result, RedirectAttributes redirectAttributes) {
        ContestValidator.validate(contest, result);
        if(result.hasErrors()) {
            return "contest/new";
        }
        contestService.createNewContest(contest);
        redirectAttributes.addFlashAttribute("alert", "Contest created successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/contests";
    }

    @GetMapping("/contests/edit/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String getEditContestPage(@PathVariable("id") long contestId, @RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = contestService.getById(contestId);
        if(null == contest) {
            redirectAttributes.addFlashAttribute("alert", "The requested contest not exits");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }

        if(!contest.getCreatorUsername().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You don't have any permission to access this page");
        }

        model.addAttribute("contest", contest);
        model.addAttribute("editContestForm", contest);
        return "contest/edit";
    }

    @GetMapping("/contests/{id}")
    public String getContestDetailsPage(@PathVariable("id") long contestId, Model model, RedirectAttributes redirectAttributes) {
        Contest existingContest = contestService.getById(contestId);
        if(null == existingContest) {
            redirectAttributes.addFlashAttribute("alert", "The requested contest not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }

        model.addAttribute("contest", existingContest);
        return "contest/details";
    }


    @PostMapping("/contests/{id}/addProblem")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String postAddProblemToContest(@PathVariable("id") long contestId,
                                          @RequestParam("username") String username,
                                          @RequestParam("problemId") long problemId,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        Contest contest = contestService.getById(contestId);
        if(null == contest) {
            redirectAttributes.addFlashAttribute("alert", "The requested contest not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }
        if(!contest.getCreatorUsername().equalsIgnoreCase(username))
            throw new AccessDeniedException("You don't have any permission to access this page");

        Problem problem = problemService.getProblemById(problemId);
        if(null == problem) {
            model.addAttribute("contest", contest);
            model.addAttribute("editContestForm", contest);
            model.addAttribute("addProblemError", "This problem not found");
            return "contest/edit";
        }

        List<Problem> existingProblemsInContest = contest.getProblems();
        for(Problem p : existingProblemsInContest) {
            if (p.getId() == problemId) {
                model.addAttribute("contest", contest);
                model.addAttribute("editContestForm", contest);
                model.addAttribute("addProblemError", "Same problem cannot be added twice in the same contest");
                return "contest/edit";
            }
        }

        contestService.addProblemToContest(contestId, problemId);

        redirectAttributes.addFlashAttribute("alert", "Problem added successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/contests/edit/{id}?username=" + username;
    }

    @PostMapping("/contests/update/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String postUpdateContest(@PathVariable("id") long contestId,
                                    @RequestParam("username") String username,
                                    @ModelAttribute("editContestForm") Contest updatedContest,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        Contest existingContest = contestService.getById(contestId);
        if(null == existingContest) {
            redirectAttributes.addFlashAttribute("alert", "The requested contest not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }
        if(!existingContest.getCreatorUsername().equalsIgnoreCase(username))
            throw new AccessDeniedException("You don't have any permission to access this page");

        ContestValidator.validate(updatedContest, result);

        if(result.hasErrors()) {
            model.addAttribute("contest", existingContest);
            return "contest/edit";
        }

        contestService.updateContestWithExisting(updatedContest, existingContest);

        redirectAttributes.addFlashAttribute("alert", "Contest updated successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/contests/edit/{id}?username=" + username;
    }

    @PostMapping("/contests/{id}/register")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String registerInContest(@PathVariable("id") long contestId,
                                    @RequestParam("username") String username,
                                    RedirectAttributes redirectAttributes) {
        Contest contest = contestService.getById(contestId);
        if(null == contest) {
            redirectAttributes.addFlashAttribute("alert", "Contest not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }
        User user = userService.getByUserName(username);
        if(null == user) {
            redirectAttributes.addFlashAttribute("alert", "User doesn't exist");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }
        contestService.registerUser(user, contest);

        redirectAttributes.addFlashAttribute("alert", "You have been registered successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/contests";
    }

    @GetMapping("/contests/{id}/problems/{problemIndex}")
    public String getProblemDetailsPage(@PathVariable("id") long contestId, @PathVariable("problemIndex") String problemIndex, Model model, RedirectAttributes redirectAttributes) {
        Problem problem = contestService.getProblemIndexInContest(contestId, problemIndex);
        if(null == problem) {
            redirectAttributes.addFlashAttribute("alert", "Problem not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/contests";
        }
        model.addAttribute("problem", problem);
        return "problem/details";
    }
}