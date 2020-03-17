package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.annotation.UniqueUsername;
import com.aymansalah.codebattle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(null == userService)
            return true;
        return !userService.isUsernameExists(username);
    }
}
