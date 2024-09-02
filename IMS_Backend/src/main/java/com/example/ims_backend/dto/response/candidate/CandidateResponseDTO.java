package com.example.ims_backend.dto.response.candidate;

import com.example.ims_backend.common.*;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.entity.Job;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
public class CandidateResponseDTO {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private String cvUrl;
    private Position position;
    private Set<Skill> skills;
    private String note;
    private CandidateStatus status;
    private Integer yearOfExperience;
    private AcademicLevel highestLevel;
    private LocalDateTime createdAt;
    private Long lastUpdatedById;

    private Long recruiterId;
    private String recruiterName;
    private Set<JobDetailsDTO> job;
}
