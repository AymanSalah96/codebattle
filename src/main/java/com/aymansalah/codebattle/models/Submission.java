package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @Column(name = "problem_index")
    private String problemIndex;

    @NotNull
    @Column(name = "contest_id")
    private long contestId;

    @NotNull
    @Column(name = "author_username")
    private String authorUsername;

    @NotNull
    @Column(name = "submission_time")
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime submissionTime;

    @NotNull
    @Column(name = "verdict")
    private String verdict;



}
