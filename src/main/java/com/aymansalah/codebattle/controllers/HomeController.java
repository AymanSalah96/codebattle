package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
