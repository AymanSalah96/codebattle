package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getHomePage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/logout-success")
    public String getLogoutSuccessPage() {
        return "redirect:/";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "register";
        }

        userService.registerNewUser(user);
        redirectAttributes.addFlashAttribute("alert", "Account created successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/login";
    }

    @PostMapping("/addAdmin")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public String postAddAdmin(@RequestParam("username") String username,
                               RedirectAttributes redirectAttributes) {
        String authUser = userService.getAuthenticatedUsername();
        User user = userService.getByUserName(username);
        if(null == user) {
            redirectAttributes.addFlashAttribute("alert", "User not found");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/edit/" + authUser;
        }

        userService.addRole(username, "ADMIN");

        redirectAttributes.addFlashAttribute("alert", username + " is admin now!" );
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/edit/" + authUser;
    }
}
