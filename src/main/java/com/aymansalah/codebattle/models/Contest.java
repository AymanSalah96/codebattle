package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

    @Column(name = "contest_name")
    private String name;

    @Column(name = "contest_duration_in_minutes")
    private int durationInMinutes;

    @Column(name = "contest_creation_date")
    private Date creationDate;

    @Column(name = "contest_start_date")
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Column(name = "contest_private")
    private int isPrivate;

    @Column(name = "contest_creator_username")
    private String creatorUsername;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable (
            name = "contests_problems",
            joinColumns = {@JoinColumn(name = "contest_id")},
            inverseJoinColumns = {@JoinColumn(name = "problem_id")}
    )
    private List<Problem> problems;


    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "contest_id")
    private List<Submission> submissions;

    @OneToMany
    @JoinColumn(name = "contest_id")
    private List<ContestParticipants> participants;

    public enum Status {
        RUNNING, FINISHED, NOT_STARTED
    }

    @Transient
    private HashMap<String, Integer> problemSolvedCountMap;

    @Transient
    private HashSet<Pair<String, String>> userSolvedProblemPairSet;

    @PostLoad
    public void updateProblemSolvedCountMap() {
        problemSolvedCountMap = new HashMap<>();
        userSolvedProblemPairSet = new HashSet<>();
        for(Submission submission : submissions) {
            if(submission.getVerdict().equalsIgnoreCase("OK")) {
                String username = submission.getAuthorUsername();
                String problemIndex = submission.getProblemIndex();
                if(!userSolvedProblemPairSet.contains(Pair.of(username, problemIndex))) {
                    problemSolvedCountMap.put(submission.getProblemIndex(), problemSolvedCountMap.getOrDefault(submission.getProblemIndex(), 0) + 1);
                }
                userSolvedProblemPairSet.add(Pair.of(username, problemIndex));
            }
        }
    }

    public boolean isUserSolvedProblem(String username, String problemIndex) {
        return userSolvedProblemPairSet.contains(Pair.of(username, problemIndex));
    }

    public Status getContestStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMinutes(durationInMinutes);
        if(startDate.compareTo(now) < 0 && now.compareTo(endDate) < 0)
            return Status.RUNNING;
        if(endDate.compareTo(now) < 0)
            return Status.FINISHED;
        return Status.NOT_STARTED;
    }

    public String getStartDateFormatted() {
        return startDate.format(DateTimeFormatter.ofPattern("LLL/dd/yyyy HH:mm"));
    }

    public int getProblemSolvedCount(String problemIndex) {
        return problemSolvedCountMap.getOrDefault(problemIndex, 0);
    }
}
