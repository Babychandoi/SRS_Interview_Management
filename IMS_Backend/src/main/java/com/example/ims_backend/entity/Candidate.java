package com.example.ims_backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.ims_backend.common.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "candidate")
@Getter
@Setter
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    private String fullName;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Professional Information
    private String cvUrl; // Lưu url file CV gửi lên
    @Enumerated(EnumType.STRING)
    private Position position;
    @ElementCollection(targetClass = Skill.class)
    @JoinTable(name = "candidate_skill", joinColumns = @JoinColumn(name = "candidate_id"))
    @Enumerated(EnumType.STRING)
    private Set<Skill> skills = new HashSet<>();
    private String note;
    @Enumerated(EnumType.STRING)
    private CandidateStatus status;
    private Integer yearOfExperience;
    @ManyToOne(fetch = FetchType.EAGER)
    private User recruiter;
    @Enumerated(EnumType.STRING)
    private AcademicLevel highestLevel;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Interview> interviews = new HashSet<>();

    // Audit Information
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @JoinTable(name = "candidate_job", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "job_id"))
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Offer> offers = new HashSet<>();
}
