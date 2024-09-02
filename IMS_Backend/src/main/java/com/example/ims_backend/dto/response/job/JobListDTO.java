package com.example.ims_backend.dto.response.job;

import java.time.LocalDate;
import java.util.Set;

import com.example.ims_backend.common.Department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobListDTO {
    private Long id;
    private String title;
    private Set<String> skills;
    private LocalDate startDate;
    private LocalDate endDate;
    private Department department;
    private Set<String> levels;
    private String status;
    private LocalDate createdAt;
}
