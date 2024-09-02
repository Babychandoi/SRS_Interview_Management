package com.example.ims_backend.service;

import com.example.ims_backend.common.CandidateStatus;
import com.example.ims_backend.dto.request.candidate.CandidateRequestDTO;
import com.example.ims_backend.dto.request.candidate.CandidateSearchDTO;
import com.example.ims_backend.dto.response.candidate.CandidateResponseDTO;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CandidateService {
    Page<CandidateResponseDTO> getAllCandidates(Pageable pageable, String keyword, CandidateStatus status);
    Optional<CandidateResponseDTO> getCandidateById(Long id);
    CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO);
    CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO);
    void deleteCandidate(Long id);
    CandidateResponseDTO banCandidate(Long id);
    Set<JobDetailsDTO> getJobsByCandidateId(Long candidateId);
    List<CandidateResponseDTO> getNotBanndedCandidates();

}
