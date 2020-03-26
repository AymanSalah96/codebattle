package com.aymansalah.codebattle.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmissionSummary {
    private String problemIndex;
    private String verdict;
    private int triesCount;
    private int acTimeInSeconds;

    public SubmissionSummary() {
        verdict = "NOT_DETERMINED";
        acTimeInSeconds = Integer.MAX_VALUE;
        triesCount = 0;
    }

}
