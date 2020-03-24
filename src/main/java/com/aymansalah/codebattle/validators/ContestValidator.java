package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.models.Contest;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

public class ContestValidator {
    public static void validate(Contest contest, BindingResult result) {
        if(CommonValidators.isNull(contest.getName()) ||
        CommonValidators.isBlank(contest.getName())) {
            result.rejectValue("name", null,"Contest name cannot be empty");
        }
        else if(contest.getName().length() < 3) {
            result.rejectValue("name", null, "Contest name length must be at least 3 characters");
        }
        if(CommonValidators.isNull(contest.getStartDate())) {
            result.rejectValue("startDate", null,"You should determine when the contest will start");
        } else {
            if(!startDateLessThan(contest.getStartDate(), 10))
                result.rejectValue("startDate", null, "You should enter a start date/time not before 10 minutes from now");
        }
        if(contest.getDurationInMinutes() < 30) {
            result.rejectValue("durationInMinutes", null, "Contest duration must be at least 30 minutes");
        }
    }

    private static boolean startDateLessThan(LocalDateTime startDate, int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).compareTo(startDate) < 0;
    }
}
