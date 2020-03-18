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
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private long id;

    @NotNull
    @Column(name = "problem_id")
    private long problemId;

    @NotNull
    @Column(name = "source_code_file_id")
    private long sourceCodeFileId;

    @NotNull
    @Column(name = "author_id")
    private long authorId;

    @NotNull
    @Column(name = "submission_time")
    private Date submissionTime;

    @NotNull
    @Column(name = "verdict")
    private String verdict;

    @NotNull
    @Column(name = "submission_output_file_id")
    private String outputFileId;
}
