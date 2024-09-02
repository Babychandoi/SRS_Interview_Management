package com.example.ims_backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ims_backend.common.JobStatus;
import com.example.ims_backend.config.SecurityUtils;
import com.example.ims_backend.dto.request.job.JobRequestDTO;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.dto.response.job.JobListDTO;
import com.example.ims_backend.entity.Job;
import com.example.ims_backend.repository.JobRepository;
import com.example.ims_backend.repository.specification.JobSpecification;
import com.example.ims_backend.service.JobService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final SecurityUtils securityUtils;

    @Override
    public Page<JobListDTO> getAll(Pageable pageable, String keyword, JobStatus status) {
        updateJobStatuses();
        Specification<Job> specification = Specification.where(JobSpecification.hasKeyword(keyword))
                .and(JobSpecification.hasStatus(status));
        Page<Job> jobs = jobRepository.findAll(specification, pageable);

        List<JobListDTO> list = jobs.stream()
                .map(this::mapToJobListDTO).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, jobs.getTotalElements());
    }

    @Override
    public JobListDTO saveJob(JobRequestDTO jobRequestDTO) {
        validateCreateJobDTO(jobRequestDTO);
        Job job = jobRepository.findById(jobRequestDTO.getId() != null ? jobRequestDTO.getId() : 0).orElse(new Job());
        job.setTitle(jobRequestDTO.getTitle());
        job.setStartDate(jobRequestDTO.getStartDate());
        job.setEndDate(jobRequestDTO.getEndDate());
        job.setSalaryStartRange(jobRequestDTO.getSalaryStartRange());
        job.setSalaryEndRange(jobRequestDTO.getSalaryEndRange());
        job.setWorkingAddress(jobRequestDTO.getWorkingAddress());
        job.setDepartment(jobRequestDTO.getDepartment());
        job.setSkills(jobRequestDTO.getSkills());
        job.setLevels(jobRequestDTO.getLevels());
        job.setBenefits(jobRequestDTO.getBenefits());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLastUpdatedBy(securityUtils.getAuthenticatedUser().getUser());
        if (jobRequestDTO.getId() == null) {
            job.setStatus(JobStatus.DRAFT);
        } else {
            job.setStatus(jobRequestDTO.getStatus());
        }
        Job savedJob = jobRepository.save(job);
        return mapToJobListDTO(savedJob);
    }

    @Override
    public Optional<JobDetailsDTO> getJobDetailsById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(this::mapToJobDetailsDTO);
    }

    public void updateJobStatuses() {
        List<Job> jobs = jobRepository.findAll();
        for (Job job : jobs) {
            if (job.getStartDate().isBefore(LocalDate.now()) && job.getEndDate().isAfter(LocalDate.now())) {
                job.setStatus(JobStatus.OPEN);
            } else if (job.getEndDate().isBefore(LocalDate.now())) {
                job.setStatus(JobStatus.CLOSE);
            } else
                job.setStatus(JobStatus.DRAFT);
        }
        jobRepository.saveAll(jobs);
    }

    private void validateCreateJobDTO(JobRequestDTO jobRequestDTO) {
        if (jobRequestDTO.getTitle() == null || jobRequestDTO.getTitle().isEmpty() ||
                jobRequestDTO.getStartDate() == null ||
                jobRequestDTO.getEndDate() == null ||
                jobRequestDTO.getSalaryStartRange() == null ||
                jobRequestDTO.getSalaryEndRange() == null ||
                jobRequestDTO.getWorkingAddress() == null || jobRequestDTO.getWorkingAddress().isEmpty() ||
                jobRequestDTO.getSkills().isEmpty() ||
                jobRequestDTO.getLevels().isEmpty() ||
                jobRequestDTO.getBenefits().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ME002: Required field");
        }
        if (jobRequestDTO.getId() == null && jobRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ME017: Start date must be later than current date");
        }
        if (jobRequestDTO.getEndDate().isBefore(jobRequestDTO.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ME018: End date must be later than Start date");
        }
    }

    private JobListDTO mapToJobListDTO(Job job) {
        JobListDTO jobListDTO = new JobListDTO();
        jobListDTO.setId(job.getId());
        jobListDTO.setTitle(job.getTitle());
        jobListDTO.setSkills(job.getSkills().stream().map(Enum::toString).collect(Collectors.toSet()));
        jobListDTO.setStartDate(job.getStartDate());
        jobListDTO.setEndDate(job.getEndDate());
        jobListDTO.setLevels(job.getLevels().stream().map(Enum::toString).collect(Collectors.toSet()));
        jobListDTO.setStatus(job.getStatus().toString());
        jobListDTO.setCreatedAt(job.getCreatedAt());
        return jobListDTO;
    }

    private JobDetailsDTO mapToJobDetailsDTO(Job job) {
        JobDetailsDTO jobDetailsDTO = new JobDetailsDTO();
        jobDetailsDTO.setId(job.getId());
        jobDetailsDTO.setTitle(job.getTitle());
        jobDetailsDTO.setSkills(job.getSkills());
        jobDetailsDTO.setStartDate(job.getStartDate());
        jobDetailsDTO.setEndDate(job.getEndDate());
        jobDetailsDTO.setSalaryStartRange(job.getSalaryStartRange());
        jobDetailsDTO.setSalaryEndRange(job.getSalaryEndRange());
        jobDetailsDTO.setDepartment(job.getDepartment());
        jobDetailsDTO.setWorkingAddress(job.getWorkingAddress());
        jobDetailsDTO.setBenefits(job.getBenefits());
        jobDetailsDTO.setLevels(job.getLevels());
        jobDetailsDTO.setDescription(job.getDescription());
        jobDetailsDTO.setStatus(job.getStatus());
        jobDetailsDTO.setCreatedAt(job.getCreatedAt());
        return jobDetailsDTO;
    }

    @Override
    public Boolean deleteJob(Long id) {
        Job existJob = jobRepository.findById(id).orElse(null);
        if (existJob != null) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
