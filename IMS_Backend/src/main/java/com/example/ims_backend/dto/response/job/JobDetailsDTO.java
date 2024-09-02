package com.example.ims_backend.dto.response.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.example.ims_backend.common.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailsDTO {
    private Long id;
    private String title;
    private Set<Skill> skills;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salaryStartRange;
    private BigDecimal salaryEndRange;
    private String workingAddress;
    private Set<Benefit> benefits;
    private Set<PositionLevel> levels;
    private Department department;
    private String description;
    private JobStatus status;
    private LocalDate createdAt;

    public JobDetailsDTO(Long id, String title, String workingAddress, String description) {
        this.id = id;
        this.title = title;
        this.workingAddress = workingAddress;
        this.description = description;
    }
}
