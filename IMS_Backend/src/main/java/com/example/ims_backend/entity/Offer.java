package com.example.ims_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.ims_backend.common.*;
import org.hibernate.annotations.CreationTimestamp;

import com.example.ims_backend.common.OfferStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "offer")
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    // @Column(name = "position", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @OneToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    // @Column(name = "contract_period_from", nullable = false)
    private LocalDate contractPeriodFrom;

    // @Column(name = "contract_period_to", nullable = false)
    private LocalDate contractPeriodTo;

    @Enumerated(EnumType.STRING)
    // @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    // @Column(name = "level", nullable = false)
    private PositionLevel level;

    @Enumerated(EnumType.STRING)
    // @Column(name = "department", nullable = false)
    private Department department;

    // @ManyToOne(optional = false)
    @ManyToOne
    private User recruiter;

    // @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // @Column(name = "basic_salary", nullable = false)
    private BigDecimal basicSalary;

    // @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    // @Column(name = "status", nullable = false)
    private OfferStatus status;

    // Audit Information
    @CreationTimestamp
    // @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    // @ManyToOne(optional = false)
    @ManyToOne
    private User lastUpdatedBy;

}
