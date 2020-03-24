package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "contest_participants")
public class ContestParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_participants_id")
    private long id;

    @Column(name = "contest_id")
    private long contestId;

    @Column(name = "participant_username")
    private String participantUsername;
}
