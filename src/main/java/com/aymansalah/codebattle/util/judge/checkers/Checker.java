package com.aymansalah.codebattle.util.judge.checkers;

import java.io.File;

public abstract class Checker {

    public abstract boolean compare(File judgeOutput, String participantOutput);

}
