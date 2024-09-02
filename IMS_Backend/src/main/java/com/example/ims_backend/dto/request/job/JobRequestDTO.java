package com.example.ims_backend.dto.request.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.example.ims_backend.common.*;
import lombok.Data;

@Data
public class JobRequestDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salaryStartRange;
    private BigDecimal salaryEndRange;
    private String workingAddress;
    private Set<Skill> skills;
    private Set<PositionLevel> levels;
    private Set<Benefit> benefits;
    private Department department;
    private JobStatus status;
    private String description;
}
