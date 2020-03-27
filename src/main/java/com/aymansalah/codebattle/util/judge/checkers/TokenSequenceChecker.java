package com.aymansalah.codebattle.util.judge.checkers;

import java.io.*;
import java.util.StringTokenizer;

public class TokenSequenceChecker extends Checker {

    private BufferedReader judgeFileReader;
    private BufferedReader participantFileReader;
    private String judgeLine;
    private String participantLine;
    private StringBuilder judgeFileContentBuilder;
    private StringBuilder participantFileContentBuilder;
    private StringTokenizer judgeTokens;
    private StringTokenizer participantTokens;

    public TokenSequenceChecker() {
        this.judgeFileContentBuilder = new StringBuilder();
        this.participantFileContentBuilder = new StringBuilder();
    }

    @Override
    public boolean compare(File judgeOutput, String participantOutput) {
        try {
            judgeFileReader = new BufferedReader(new FileReader(judgeOutput));
            participantFileReader = new BufferedReader(new StringReader(participantOutput));

            while(null != (judgeLine = judgeFileReader.readLine())) {
                judgeFileContentBuilder.append(judgeLine);
                judgeFileContentBuilder.append(" ");
            }
            while(null != (participantLine = participantFileReader.readLine())) {
                participantFileContentBuilder.append(participantLine);
                participantFileContentBuilder.append(" ");
            }

            judgeTokens = new StringTokenizer(judgeFileContentBuilder.toString());
            participantTokens = new StringTokenizer(participantFileContentBuilder.toString());

            if(judgeTokens.countTokens() != participantTokens.countTokens())
                return false;
            while(participantTokens.hasMoreTokens())
                if(!participantTokens.nextToken().equals(judgeTokens.nextToken()))
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
