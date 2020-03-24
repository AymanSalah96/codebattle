package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.Problem;
import com.aymansalah.codebattle.models.Submission;
import com.aymansalah.codebattle.services.*;
import com.aymansalah.codebattle.util.judge.Helper;
import com.aymansalah.codebattle.util.judge.checkers.Checker;
import com.aymansalah.codebattle.util.judge.checkers.LineChecker;
import com.aymansalah.codebattle.validators.FileUploadValidator;
import com.aymansalah.codebattle.validators.ProblemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ProblemController {

    @Autowired
    UserService userService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SubmissionService submissionService;

    @InitBinder
    public void initBinder ( WebDataBinder binder ) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

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

    @GetMapping("/problems/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getProblemDetails(@PathVariable("id") long problemId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        Problem problem = problemService.getProblemById(problemId);
        if(null == problem) {
            redirectAttributes.addFlashAttribute("alert", "Problem not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/";
        }
        String username = userService.getAuthenticatedUsername();
        if(!problem.getCreatorUsername().equalsIgnoreCase(username))
            throw new AccessDeniedException("You don't have any permission to access this page");

        model.addAttribute("problem", problem);

        return "problem/details";
    }


    @PostMapping("/problems/new")
    @PreAuthorize("isAuthenticated()")
    public String postNewProblem(@RequestParam("ioFiles")MultipartFile[] ioFiles,
                                 @ModelAttribute("problem") Problem problem,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        ProblemValidator.validate(problem, result);
        List<String> ioFilesErrors = FileUploadValidator.validateProblemIOFiles(ioFiles);
        if(result.hasErrors() || !ioFilesErrors.isEmpty()) {
            model.addAttribute("ioFilesErrors", ioFilesErrors);
            return "problem/new";
        }
        problemService.createNewProblem(problem, ioFiles);
        redirectAttributes.addFlashAttribute("alert", "Problem created successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/problems";
    }

    @GetMapping("/problems/author/{username}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String getUserOwnProblemsPage(@PathVariable("username") String username, Model model) {
        List<Problem> problemList = problemService.getProblemsCreatedByUsername(username);
        model.addAttribute("problemsList", problemList);
        return "problem/author";
    }

    @GetMapping("/problems/edit/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String getEditProblemPage(@PathVariable("id") long problemId, @RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {
        Problem problem = problemService.getProblemById(problemId);

        if(null == problem) {
            redirectAttributes.addFlashAttribute("alert", "Problem not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/problems";
        }

        if(!problem.getCreatorUsername().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You don't have any permission to access this page");
        }

        model.addAttribute("problem", problem);
        model.addAttribute("problemEditForm", problem);
        return "problem/edit";
    }

    @GetMapping("/problems/downloadInputFiles/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public void getInputFiles(@PathVariable("id") long problemId,
                                @RequestParam("username") String username,
                                HttpServletResponse response,
                                RedirectAttributes redirectAttributes) {
        Problem problem = problemService.getProblemById(problemId);
        if(null == problem)
            return;

        // TODO: handle premessions if the contest is running .....

        Path file = problemService.getMainInputTestFile(problem);
        if(null == file || !Files.exists(file)) {
            return;
        }

        response.setContentType("text/plain");
        String fileName = new String();
        if(problem.getIndex() == null)
            fileName = problem.getName();
        else
            fileName = problem.getIndex();
        response.addHeader("Content-Disposition", "attachment; filename="+fileName+".in");
        try {
            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @PostMapping("/problems/submitProblem/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String submitProblem(@PathVariable("id") long problemId,
                                @RequestParam("username") String username,
                                @RequestParam("outputFile") MultipartFile file,
                                RedirectAttributes redirectAttributes) {


        boolean result = submissionService.submit(problemId, username, file);

        redirectAttributes.addFlashAttribute("submissionStatus", result);
        return "redirect:/problems/{id}";
        /*
        // validate user
        List<String> errors = FileUploadValidator.validateSubmitOutputFile(file);
        if(!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("outputFileSubmitErrors", errors);
            return "redirect:/problems/{id}";
        }

        return "redirect:/problems/{id}";
        */
    }

    @PostMapping("/problems/edit/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public String postEditProblem(@PathVariable("id") long problemId,
                                  @RequestParam("ioFiles")MultipartFile[] ioFiles,
                                  @RequestParam("username") String username,
                                  @ModelAttribute("problemEditForm") Problem problem,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Problem existingProblem = problemService.getProblemById(problemId);
        if(null == existingProblem) {
            redirectAttributes.addFlashAttribute("alert", "Problem not found");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/problems";
        }

        if(!existingProblem.getCreatorUsername().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You don't have any permission to access the requested page");
        }

        ProblemValidator.validate(problem, result);
        if (result.hasErrors()) {
            model.addAttribute("problem", existingProblem);
            return "problem/edit";
        }
        if(ioFiles.length > 0 && !ioFiles[0].getOriginalFilename().isEmpty()) {
            List<String> ioFilesErrors = FileUploadValidator.validateProblemIOFiles(ioFiles);
            model.addAttribute("ioFilesErrors", ioFilesErrors);
            return "problem/edit";
        }
        Helper.replaceNotNullProperties(existingProblem, problem);
        problemService.editProblem(existingProblem, ioFiles);
        redirectAttributes.addFlashAttribute("alert", "All changes saved successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/problems/edit/{id}?username=" + username;
    }

}
