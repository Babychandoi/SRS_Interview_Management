package com.example.ims_backend.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.text.Position;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.ims_backend.common.CandidateStatus;
import com.example.ims_backend.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    Optional<Candidate> findAllByFullNameAndPosition(String fullName, Position position);

    List<Candidate> findAllByStatusNot(CandidateStatus candidateStatus);

}
