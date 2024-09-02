package com.example.ims_backend.dto.response.offer;

import com.example.ims_backend.common.*;
import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.InterviewDTO;
import com.example.ims_backend.dto.UserDTO;
import com.example.ims_backend.entity.Candidate;
import com.example.ims_backend.entity.Interview;
import com.example.ims_backend.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OfferDetailsResponseDTO {
    private Long id;
    private CandidateDTO candidate;

    private Position position;
    private String positionName;

    private UserDTO approver;
    private InterviewDTO interview;
    private LocalDate contractPeriodFrom;
    private LocalDate contractPeriodTo;

    private ContractType contractType;
    private String contractTypeName;

    private PositionLevel level;
    private String levelName;

    private OfferStatus status;
    private String statusName;

    private Department department;
    private String departmentName;

    private User recruiter;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String note;
    private LocalDate createdAt;
    private User lastUpdatedBy;
}
