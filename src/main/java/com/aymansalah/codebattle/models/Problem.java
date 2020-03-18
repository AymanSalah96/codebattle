package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @Column(name = "problem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "problem_name")
    private String name;

    @NotNull
    @Column(name = "problem_index")
    private String index;

    @NotNull
    @Column(name = "problem_input")
    private String inputDescription;

    @NotNull
    @Column(name = "problem_output")
    private String outputDescription;

    @NotNull
    @Column(name = "problem_description")
    private String description;

    @NotNull
    @Column(name = "problem_judge_solution_file_id")
    private long judgeSolutionFileId;

    @NotNull
    @Column(name = "problem_checker_id")
    private int checkerId;

    @NotNull
    @Column(name = "problem_timer")
    private int timerInSeconds;

    @NotNull
    @Column(name = "problem_creation_date")
    private Date creationDate;

    @NotNull
    @Column(name = "problem_contest_id")
    private long contestId;

    @NotNull
    @Column(name = "problem_score")
    private int score;

    @NotNull
    @Column(name = "problem_input_file_id")
    private int inputFileId;
}
