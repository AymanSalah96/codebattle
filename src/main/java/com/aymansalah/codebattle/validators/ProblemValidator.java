package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.models.Problem;
import org.springframework.validation.BindingResult;

import java.util.Date;

public class ProblemValidator {
    // TODO: Complete this method
    public static void validate(Problem problem, BindingResult result) {
        validateName(problem.getName(), result);
        validateInputDescription(problem.getInputDescription(), result);
        validateOutputDescription(problem.getOutputDescription(), result);
        validateDescription(problem.getDescription(), result);
        validateCheckerId(problem.getCheckerId(), result);
        validateTimer(problem.getTimerInSeconds(), result);
        validateCreationDate(problem.getCreationDate(), result);
        validateScore(problem.getScore(), result);
        validateNotes(problem.getNotes(), result);
        validateTutorial(problem.getTutorial(), result);
    }

    private static void validateTutorial(String tutorial, BindingResult result) {
    }

    private static void validateNotes(String notes, BindingResult result) {
    }

    private static void validateScore(int score, BindingResult result) {
    }

    private static void validateCreationDate(Date creationDate, BindingResult result) {
    }

    private static void validateTimer(int timerInSeconds, BindingResult result) {
    }

    private static void validateCheckerId(int checkerId, BindingResult result) {
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
