package com.example.ims_backend.dto.request.candidate;

import com.example.ims_backend.common.CandidateStatus;
import lombok.Data;

@Data
public class CandidateSearchDTO {
    private String keyword; // Search keyword for fullName, email, phoneNumber, position, recruiter
    private CandidateStatus status; // Search by status
}
