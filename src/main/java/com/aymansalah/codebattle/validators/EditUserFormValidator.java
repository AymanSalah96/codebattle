package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.models.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EditUserFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        validateUsername(user.getUsername(), errors);
        validateFirstName(user.getFirstName(), errors);
        validateLastName(user.getLastName(), errors);
        validateCity(user.getCity(), errors);
        validateCountryCode(user.getCountryCode(), errors);
        validateEmail(user.getEmail(), errors);
        validateInstitute(user.getInstitute(), errors);
    }


    private void validateEmail(String email, Errors errors) {
        if(isNull(email)) {
            errors.rejectValue("email", null, "Email cannot be null");
            return;
        }
        if(isBlank(email)) {
            errors.rejectValue("email", null, "Email cannot be empty");
            return;
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(email)) {
            errors.rejectValue("email", null, "Enter a valid email");
            return;
        }
        if(email.trim().length() > 128) {
            errors.rejectValue("email", null,"Email size cannot exceed 128 characters");
            return;
        }
    }

    private void validateInstitute(String institute, Errors errors) {
        if(isNull(institute))
            return;
        if(institute.trim().length() > 128) {
            errors.rejectValue("institute", null, "Institute size cannot exceed 128 characters");
        }
    }

    private void validateCountryCode(String countryCode, Errors errors) {
        if(isNull(countryCode))
            return;
        if(isBlank(countryCode)) {
            errors.rejectValue("countryCode", null, "Country code cannot be blank");
            return;
        }
        if(countryCode.trim().length() > 2) {
            errors.rejectValue("countryCode", null, "Country code size cannot exceed two characters");
        }
    }

    private void validateCity(String city, Errors errors) {
        if(isNull(city))
            return;
        if(isBlank(city)) {
            errors.rejectValue("city", null, "City cannot be blank");
            return;
        }
        if(city.trim().length() > 128) {
            errors.rejectValue("city", null, "City size cannot exceed 128 characters");
        }
    }

    private void validateLastName(String lastName, Errors errors) {
        if(isNull(lastName)) {
            errors.rejectValue("lastName", null, "Last name cannot be null");
            return;
        }
        if(isBlank(lastName)) {
            errors.rejectValue("lastName", null, "Last name cannot be empty");
            return;
        }
        if(lastName.trim().length() > 128) {
            errors.rejectValue("lastName", null, "Last name size cannot exceed 128 characters");
        }
    }

    private void validateFirstName(String firstName, Errors errors) {
        if(isNull(firstName)) {
            errors.rejectValue("firstName", null, "First name cannot be null");
            return;
        }
        if(isBlank(firstName)) {
            errors.rejectValue("firstName", null, "First name cannot be empty");
            return;
        }
        if(firstName.trim().length() > 128) {
            errors.rejectValue("firstName", null, "First name size cannot exceed 128 characters");
        }
    }

    private void validateUsername(String username, Errors errors) {
        if(isNull(username)) {
            errors.rejectValue("username", null, "Username cannot be null");
            return;
        }
        if(isBlank(username)) {
            errors.rejectValue("username", null, "Username cannot be empty");
            return;
        }
        if(username.trim().length() > 128) {
            errors.rejectValue("username", null, "Username size cannot exceed 128 characters");
        }
        if(username.trim().length() < 3) {
            errors.rejectValue("username", null, "Username size must be at least 3 characters");
        }
    }

    private boolean isNull(Object o) {
        return null == o;
    }

    private boolean isBlank(String s) {
        s = s.replaceAll("\\s", "");
        return s.isEmpty();
    }
}
