package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "contests_problems")
public class ContestProblems implements Comparable<ContestProblems>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contests_problems_id")
    private long id;

    @Column(name = "contest_id")
    private long contestId;

    @Column(name = "problem_id")
    private long problemId;

    @Column(name = "problem_index")
    private int problemIdx;


    @Override
    public int compareTo(ContestProblems target) {
        return Integer.valueOf(problemIdx).compareTo(target.problemIdx);
    }

    public String idxToString() {
        int A = this.problemIdx;
        StringBuilder sb = new StringBuilder();
        A--;
        while(A >= 0) {
            int tmp = A % 26;
            sb.append((char) (tmp + 'A'));
            A /= 26;
            A--;
        }
        return sb.reverse().toString();
    }
}
