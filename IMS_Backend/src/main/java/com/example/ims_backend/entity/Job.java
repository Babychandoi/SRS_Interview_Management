package com.example.ims_backend.entity;

import com.example.ims_backend.common.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salaryStartRange;
    private BigDecimal salaryEndRange;
    private String workingAddress;

    @ElementCollection(targetClass = Skill.class)
    @JoinTable(name = "job_skill",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Skill> skills = new HashSet<>();

    @ElementCollection(targetClass = PositionLevel.class)
    @JoinTable(name = "job_position_level",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<PositionLevel> levels = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Department department;

    @ElementCollection(targetClass = Benefit.class)
    @JoinTable(name = "job_benefit",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Benefit> benefits = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private String description;

    // Audit Information
    @CreationTimestamp
    private LocalDate createdAt;
    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany(mappedBy = "jobs")
    private Set<Candidate> candidates = new HashSet<>();
}
