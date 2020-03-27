package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.models.Problem;
import org.springframework.validation.BindingResult;

public class ProblemValidator {

    public static void validate(Problem problem, BindingResult result) {
        validateName(problem.getName(), result);
        validateInputDescription(problem.getInputDescription(), result);
        validateOutputDescription(problem.getOutputDescription(), result);
        validateDescription(problem.getDescription(), result);
    }

    private static void validateDescription(String description, BindingResult result) {
        if(CommonValidators.isNull(description) || CommonValidators.isBlank(description))
            result.rejectValue("description", null, "Description cannot be empty");
    }

    private static void validateOutputDescription(String outputDescription, BindingResult result) {
        if(CommonValidators.isNull(outputDescription) || CommonValidators.isBlank(outputDescription))
            result.rejectValue("outputDescription", null, "Output description cannot be empty");
    }

    private static void validateInputDescription(String inputDescription, BindingResult result) {
        if(CommonValidators.isNull(inputDescription) || CommonValidators.isBlank(inputDescription))
            result.rejectValue("inputDescription", null, "Input description cannot be empty");
    }

    private static void validateName(String name, BindingResult result) {
        if(CommonValidators.isNull(name) || CommonValidators.isBlank(name))
            result.rejectValue("name", null, "Name cannot be empty");
    }

}
