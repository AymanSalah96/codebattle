package com.aymansalah.codebattle.util.judge.checkers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class Checker {


    private enum CheckerTypes {
        LCMP, WCMP
    }

    public abstract boolean compare(File judgeOutput, File participantOutput);

}
