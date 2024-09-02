package com.example.ims_backend.controller;

import com.example.ims_backend.common.*;
import com.example.ims_backend.dto.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/commons")
@RequiredArgsConstructor
public class CommonController {

    @GetMapping("/departments")
    public ResponseEntity<ResponseData<?>> getDepartments() {
        Map<String, String> departments = new HashMap<>();
        for (Department d : Department.values()) {
            departments.put(d.name(), d.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(departments, "Departments retrieved successfully"));
    }

    @GetMapping("/offer-statuses")
    public ResponseEntity<ResponseData<?>> getOfferStatuses() {
        Map<String, String> offerStatus = new HashMap<>();
        for (OfferStatus os : OfferStatus.values()) {
            offerStatus.put(os.name(), os.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(offerStatus, "Offer status retrieved successfully"));
    }

    @GetMapping("/positions")
    public ResponseEntity<ResponseData<?>> getPositions() {
        Map<String, String> positions = new HashMap<>();
        for (Position p : Position.values()) {
            positions.put(p.name(), p.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(positions, "Positions retrieved successfully"));
    }

    @GetMapping("/contract-types")
    public ResponseEntity<ResponseData<?>> getContractTypes() {
        Map<String, String> contractTypes = new HashMap<>();
        for (ContractType ct : ContractType.values()) {
            contractTypes.put(ct.name(), ct.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(contractTypes, "Contract types retrieved successfully"));
    }

    @GetMapping("/position-levels")
    public ResponseEntity<ResponseData<?>> getPositionLevels() {
        Map<String, String> positionLevels = new HashMap<>();
        for (PositionLevel pl : PositionLevel.values()) {
            positionLevels.put(pl.name(), pl.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(positionLevels, "Position levels retrieved successfully"));
    }

    @GetMapping("/job-statuses")
    public ResponseEntity<ResponseData<?>> getJobStatuses() {
        Map<String, String> jobStatuses = new HashMap<>();
        for (JobStatus js : JobStatus.values()) {
            jobStatuses.put(js.name(), js.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(jobStatuses, "Job statuses retrieved successfully"));
    }

    @GetMapping("/candidate-statuses")
    public ResponseEntity<ResponseData<?>> getCandidateStatuses() {
        Map<String, String> candidateStatuses = new HashMap<>();
        for (CandidateStatus cs : CandidateStatus.values()) {
            candidateStatuses.put(cs.name(), cs.getValue());
        }
        return ResponseEntity.ok(new ResponseData<>(candidateStatuses, "Candidate statuses retrieved successfully"));
    }
}
