package com.example.ims_backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ims_backend.common.CandidateStatus;
import com.example.ims_backend.config.SecurityUtils;
import com.example.ims_backend.dto.request.candidate.CandidateRequestDTO;
import com.example.ims_backend.dto.response.candidate.CandidateResponseDTO;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.entity.Candidate;
import com.example.ims_backend.entity.Job;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.repository.CandidateRepository;
import com.example.ims_backend.repository.JobRepository;
import com.example.ims_backend.repository.specification.CandidateSpecification;
import com.example.ims_backend.service.CandidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final SecurityUtils securityUtils;
    private final UserServiceImpl userService;

    @Override
    public Page<CandidateResponseDTO> getAllCandidates(Pageable pageable, String keyword, CandidateStatus status) {
        Specification<Candidate> spec = Specification.where(CandidateSpecification.hasKeyword(keyword))
                .and(CandidateSpecification.hasStatus(status));

        Page<Candidate> candidates = candidateRepository.findAll(spec, pageable);
        List<CandidateResponseDTO> dtos = candidates.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, candidates.getTotalElements());
    }

    @Override
    public Optional<CandidateResponseDTO> getCandidateById(Long id) {
        return candidateRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO) {
        Candidate candidate = convertToEntity(candidateRequestDTO);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return convertToDto(savedCandidate);
    }

    @Override
    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        updateEntityFromDto(candidate, candidateRequestDTO);
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return convertToDto(updatedCandidate);
    }

    @Override
    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidateRepository.delete(candidate);
    }

    @Override
    public CandidateResponseDTO banCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidate.setStatus(CandidateStatus.BANNED);
        Candidate bannedCandidate = candidateRepository.save(candidate);
        return convertToDto(bannedCandidate);
    }

    @Override
    public List<CandidateResponseDTO> getNotBanndedCandidates() {
        List<Candidate> candidates = candidateRepository.findAllByStatusNot(CandidateStatus.BANNED);
        return candidates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CandidateResponseDTO convertToDto(Candidate candidate) {
        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(candidate.getId());
        dto.setFullName(candidate.getFullName());
        dto.setDob(candidate.getDob());
        dto.setEmail(candidate.getEmail());
        dto.setPhoneNumber(candidate.getPhoneNumber());
        dto.setAddress(candidate.getAddress());
        dto.setGender(candidate.getGender());
        dto.setCvUrl(candidate.getCvUrl());
        dto.setPosition(candidate.getPosition());
        dto.setSkills(candidate.getSkills());
        dto.setNote(candidate.getNote());
        dto.setStatus(candidate.getStatus());
        dto.setYearOfExperience(candidate.getYearOfExperience());
        if (candidate.getRecruiter() != null) {
            dto.setRecruiterId(candidate.getRecruiter().getId());
            dto.setRecruiterName(candidate.getRecruiter().getFullName()); // Thiết lập tên recruiter
        }
        dto.setHighestLevel(candidate.getHighestLevel());
        dto.setCreatedAt(candidate.getCreatedAt());
        dto.setLastUpdatedById(candidate.getLastUpdatedBy() != null ? candidate.getLastUpdatedBy().getId() : null);
        Set<JobDetailsDTO> jobDTOs = candidate.getJobs().stream()
                .map(job -> {
                    JobDetailsDTO jobDTO = new JobDetailsDTO();
                    jobDTO.setId(job.getId());
                    jobDTO.setTitle(job.getTitle());
                    return jobDTO;
                })
                .collect(Collectors.toSet());

        dto.setJob(jobDTOs);
        return dto;
    }

    private Candidate convertToEntity(CandidateRequestDTO dto) {
        User recruiter = userService.findById(dto.getRecruiter().getId());
        Candidate candidate = new Candidate();
        candidate.setFullName(dto.getFullName());
        candidate.setDob(dto.getDob());
        candidate.setEmail(dto.getEmail());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setAddress(dto.getAddress());
        candidate.setGender(dto.getGender());
        candidate.setCvUrl(dto.getCvUrl());
        candidate.setPosition(dto.getPosition());
        candidate.setSkills(dto.getSkills());
        candidate.setNote(dto.getNote());
        candidate.setYearOfExperience(dto.getYearOfExperience());
        candidate.setStatus(CandidateStatus.OPEN);
        candidate.setHighestLevel(dto.getHighestLevel());
        candidate.setRecruiter(recruiter);
        candidate.setLastUpdatedBy(securityUtils.getAuthenticatedUser().getUser());
        Set<Job> jobs = new HashSet<>();
        for (String jobTitle : dto.getJob()) {
            Optional<Job> jobOptional = jobRepository.findByTitle(jobTitle);
            if (jobOptional.isPresent()) {
                jobs.add(jobOptional.get()); // Thêm Job vào Set
            } else {
                System.out.println("Job not found: " + jobTitle); // Kiểm tra log
            }
        }
        candidate.setJobs(jobs);

        return candidate;
    }

    private void updateEntityFromDto(Candidate candidate, CandidateRequestDTO dto) {
        User recruiter = userService.findById(dto.getRecruiter().getId());
        candidate.setFullName(dto.getFullName());
        candidate.setDob(dto.getDob());
        candidate.setEmail(dto.getEmail());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setAddress(dto.getAddress());
        candidate.setGender(dto.getGender());
        candidate.setCvUrl(dto.getCvUrl());
        candidate.setPosition(dto.getPosition());
        candidate.setSkills(dto.getSkills());
        candidate.setNote(dto.getNote());
        candidate.setYearOfExperience(dto.getYearOfExperience());
        candidate.setRecruiter(recruiter);
        candidate.setHighestLevel(dto.getHighestLevel());
        candidate.setLastUpdatedBy(securityUtils.getAuthenticatedUser().getUser());
    }

    @Override
    public Set<JobDetailsDTO> getJobsByCandidateId(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate != null) {
            return candidate.getJobs().stream()
                    .map(job -> new JobDetailsDTO(job.getId(), job.getTitle(), job.getWorkingAddress(),
                            job.getDescription()))
                    .collect(Collectors.toSet());
        } else {
            throw new RuntimeException("Candidate not found with ID: " + candidateId);
        }
    }

}
