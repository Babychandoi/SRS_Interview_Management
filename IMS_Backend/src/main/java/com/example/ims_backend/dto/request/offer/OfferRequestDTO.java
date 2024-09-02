package com.example.ims_backend.dto.request.offer;

import com.example.ims_backend.common.*;
import com.example.ims_backend.entity.Candidate;
import com.example.ims_backend.entity.Interview;
import com.example.ims_backend.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OfferRequestDTO {
    private Long id;
    private Candidate candidate;   // Id
    private Position position; // Enum
    private User approver;  // Id
    private Interview interview;   // Id
    private LocalDate contractPeriodFrom;
    private LocalDate contractPeriodTo;
    private ContractType contractType;  // Enum
    private PositionLevel level;    // Enum
    private OfferStatus status;   // Enum
    private Department department; // Enum
    private User recruiter; //  Id
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String note;
    private LocalDate createdAt;
    private User lastUpdatedBy;    // Id
}
