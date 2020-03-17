package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.annotation.UniqueEmail;
import com.aymansalah.codebattle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(null == userService)
            return true;
        return !userService.isEmailExists(email);
    }
}
