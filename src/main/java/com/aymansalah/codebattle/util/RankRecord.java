package com.aymansalah.codebattle.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class RankRecord implements Comparable<RankRecord> {
    private String username;
    private Integer penalty;
    private Integer solved;
    private HashMap<String, SubmissionSummary> problemSummaryMap;
    private Integer rank;

    public RankRecord(String username) {
        this.username = username;
        penalty = 0;
        solved = 0;
        rank = 0;
        problemSummaryMap = new HashMap<>();
    }

    @Override
    public int compareTo(RankRecord o) {
        if(this.solved == o.solved) {
            if(this.penalty == o.penalty)
                return this.username.compareTo(o.username);
            return this.penalty.compareTo(o.penalty);
        }
        return o.solved.compareTo(this.solved);
    }
}
