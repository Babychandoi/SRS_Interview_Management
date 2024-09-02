package com.example.ims_backend.controller;

import com.example.ims_backend.common.JobStatus;
import com.example.ims_backend.dto.request.job.JobRequestDTO;
import com.example.ims_backend.dto.response.ResponseData;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.dto.response.job.JobListDTO;
import com.example.ims_backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<JobListDTO>>> getAllJobs(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobStatus status) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobListDTO> jobs = jobService.getAll(pageable, keyword, status);
        return new ResponseEntity<>(new ResponseData<>(jobs, "Jobs retrieved successfully"), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseData<JobListDTO>> createJob(@RequestBody JobRequestDTO jobRequestDTO) {
        // Validate and create job logic here
        JobListDTO job = jobService.saveJob(jobRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseData<>(job, "Job created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseData<JobListDTO>> updateJob(@RequestBody JobRequestDTO jobRequestDTO) {
        // Validate and update job logic here
        JobListDTO job = jobService.saveJob(jobRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseData<>(job, "Job updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<JobDetailsDTO>> getJobDetails(@PathVariable Long id) {
        Optional<JobDetailsDTO> jobDetailsDTO = jobService.getJobDetailsById(id);
        return jobDetailsDTO.map(jobDetails -> ResponseEntity
                        .ok(new ResponseData<>(jobDetails, "Job retrieved successfully")))
                .orElseGet(() -> new ResponseEntity<>(new ResponseData<>(null, "Job not found"), HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        Boolean flag = jobService.deleteJob(id);

        if (flag) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(null, "Job not found"));
        }
    }

}
