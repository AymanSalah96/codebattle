package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "contests")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private long id;

    @Column(name = "contest_duration_in_seconds")
    private int durationInSeconds;

    @Column(name = "contest_creation_date")
    private Date creationDate;

    @OneToMany
    @JoinColumn(name = "problem_contest_id")
    private List<Problem> problems;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable (
            name = "contest_participants",
            joinColumns = {@JoinColumn(name = "contest_id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id")}
    )
    private List<User> participants;
}
