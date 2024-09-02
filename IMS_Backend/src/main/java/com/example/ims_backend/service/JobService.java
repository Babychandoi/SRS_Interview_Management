package com.example.ims_backend.service;

import com.example.ims_backend.common.JobStatus;
import com.example.ims_backend.dto.request.job.JobRequestDTO;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.dto.response.job.JobListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobService {
    Page<JobListDTO> getAll(Pageable pageable, String keyword, JobStatus status);
    JobListDTO saveJob(JobRequestDTO jobRequestDTO);
    Optional<JobDetailsDTO> getJobDetailsById(Long id);
    Boolean deleteJob(Long id);
}
