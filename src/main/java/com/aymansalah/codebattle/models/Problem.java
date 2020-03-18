package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Column(name = "problem_name")
    private String name;

    @Column(name = "problem_index")
    private String index;

    @NotNull
    @NotBlank
    @Column(name = "problem_input")
    private String inputDescription;

    @NotNull
    @NotBlank
    @Column(name = "problem_output")
    private String outputDescription;

    @NotNull
    @NotBlank
    @Column(name = "problem_description")
    private String description;

    @NotNull
    @Column(name = "problem_checker_id")
    private int checkerId;

    @NotNull
    @Column(name = "problem_timer")
    private int timerInSeconds;

    @Column(name = "problem_creation_date")
    private Date creationDate;

    @Column(name = "problem_contest_id")
    private long contestId;

    @NotNull
    @Column(name = "problem_score")
    private int score;

    @Column(name = "problem_notes")
    private String notes;

    @Column(name = "problem_tutorial")
    private String tutorial;

    @Column(name = "problem_creator_username")
    private String creatorUsername;

    @OneToMany
    @JoinColumn(name = "problem_id")
    private List<InputOutputFile> inputOutputFiles;

    @OneToMany
    @JoinColumn(name = "author_id")
    private List<Submission> submissions;

    public int getACSubmissionsCount() {
        int count = 0;
        for(Submission submission : submissions)
            count += submission.getVerdict().equalsIgnoreCase("OK") ? 1 : 0;
        return count;
    }
}
