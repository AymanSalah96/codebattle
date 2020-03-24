package com.aymansalah.codebattle.util.judge.checkers;

import java.io.*;

public class LineChecker extends Checker {

    private BufferedReader judgeFileReader;
    private BufferedReader participantFileReader;
    private String judgeLine;
    private String participantLine;

    @Override
    public boolean compare(File judgeOutput, File participantOutput) {
        try {
            judgeFileReader = new BufferedReader(new FileReader(judgeOutput));
            participantFileReader = new BufferedReader(new FileReader(participantOutput));

            while(true) {
                participantLine = participantFileReader.readLine();
                judgeLine = judgeFileReader.readLine();
                if(null == participantLine || null == judgeLine)
                    break;
                if(!participantLine.equals(judgeLine))
                    return false;
            }

            if(null != judgeLine || null != participantLine)
                return false;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                judgeFileReader.close();
                participantFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
