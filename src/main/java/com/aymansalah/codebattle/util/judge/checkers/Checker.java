package com.aymansalah.codebattle.util.judge.checkers;

import java.io.File;

public interface Checker {

    boolean compare(File judgeOutput, File participantOutput);
}
