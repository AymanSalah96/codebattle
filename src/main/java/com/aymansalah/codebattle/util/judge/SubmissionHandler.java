package com.aymansalah.codebattle.util.judge;

import com.aymansalah.codebattle.util.judge.checkers.Checker;

import java.io.File;

public class SubmissionHandler {

    public boolean isAccepted(File judgeOutput, File participantOutput, Checker checker) {
        return checker.compare(judgeOutput, participantOutput);
    }

}
