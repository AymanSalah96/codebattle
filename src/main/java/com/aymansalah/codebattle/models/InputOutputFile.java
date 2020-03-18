package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "problem_io_files")
public class InputOutputFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_io_file_id")
    private long id;

    @Column(name = "problem_id")
    private long problemId;

    @Column(name = "input_file_id")
    private long inputFileId;

    @Column(name = "output_file_id")
    private long outputFileId;

}
