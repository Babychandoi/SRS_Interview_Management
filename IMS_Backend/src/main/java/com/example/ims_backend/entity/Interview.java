package com.example.ims_backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.example.ims_backend.common.InterviewResult;
import com.example.ims_backend.common.InterviewStatus;
import com.example.ims_backend.common.PositionLevel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "interview")
@Getter
@Setter
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Candidate candidate;
    private LocalDate interviewDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    @ManyToOne(fetch = FetchType.EAGER)
    private Job job;
    @ManyToMany
    @JoinTable(name = "interview_interviewers",
            joinColumns = @JoinColumn(name = "interview_id"),
            inverseJoinColumns = @JoinColumn(name = "interviewers_id"))
    private Set<User> interviewers = new HashSet<>();
    private String location;

    @ManyToOne
    private User recruiter;
    private String meetingId;
    private String note;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;
    @Enumerated(EnumType.STRING)
    private PositionLevel positionLevel;
    @Enumerated(EnumType.STRING)
    private InterviewResult result;
}
