package com.aymansalah.codebattle.controllers;

import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.services.CountryService;
import com.aymansalah.codebattle.services.FileUploadService;
import com.aymansalah.codebattle.services.UserService;
import com.aymansalah.codebattle.validators.EditUserFormValidator;
import com.aymansalah.codebattle.validators.ImageUploadValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private FileUploadService fileUploadService;

    @InitBinder
    public void initBinder ( WebDataBinder binder ) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/profile/{username}")
    public String getProfilePage(@PathVariable String username, Model model, RedirectAttributes redirectAttributes) {
        if(!userService.isUsernameExists(username)) {
            redirectAttributes.addFlashAttribute("alert", "The requested profile doesn't exist");
            redirectAttributes.addFlashAttribute("alertType", "primary");
            return "redirect:/";
        }
        model.addAttribute("user", userService.getByUserName(username));
        return "profile/index";
    }

    @GetMapping("/edit/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String getEditPage(@PathVariable String username, Model model, RedirectAttributes redirectAttributes) {
        User existingUser = userService.getByUserName(username);
        model.addAttribute("user", existingUser);
        model.addAttribute("editUserForm", existingUser);
        model.addAttribute("countries", countryService.getAllCountries());
        return "profile/edit";
    }

    @PostMapping("/edit/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String postEditPage(@PathVariable String username, @ModelAttribute("editUserForm") User editUserForm, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "profile/edit";
        }
        User existingUser = userService.getByUserName(username);

        new EditUserFormValidator().validate(editUserForm, result);
        // Check if email is changed and the new one is already exists in the database
        if(null != editUserForm.getEmail() && !existingUser.getEmail().equalsIgnoreCase(editUserForm.getEmail())
        && this.userService.isEmailExists(editUserForm.getEmail()))
            result.rejectValue("email", null,"This email already exists");
        // Check if username is changed and the new one is already exists in the database
        if(null != editUserForm.getUsername() && !existingUser.getUsername().equalsIgnoreCase(editUserForm.getUsername())
                && this.userService.isUsernameExists(editUserForm.getUsername()))
            result.rejectValue("email", null,"This email already exists");

        if(result.hasErrors()) {
            model.addAttribute("user", existingUser);
            return "profile/edit";
        }
        userService.update(username, editUserForm);

        redirectAttributes.addFlashAttribute("alert", "All changes saved successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/edit/{username}";
    }

    @PostMapping("/upload-profile-image/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String uploadPhoto(@PathVariable("username") String username, @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes) {
        List<String> errors = ImageUploadValidator.validateImage(imageFile);
        if(!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileUploadErrors", errors);
            return "redirect:/edit/{username}";
        }
        try {
            fileUploadService.savePhotoForUsername(imageFile, username);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("alert", "An error has occurred during uploading the file, please try again");
            redirectAttributes.addFlashAttribute("alertType", "primary ");
            return "redirect:/edit/{username}";
        }
        redirectAttributes.addFlashAttribute("alert", "Your photo is updated successfully");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/edit/{username}";
    }


}
