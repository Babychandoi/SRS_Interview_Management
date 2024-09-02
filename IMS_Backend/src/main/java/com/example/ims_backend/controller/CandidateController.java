package com.example.ims_backend.controller;

import com.example.ims_backend.common.CandidateStatus;
import com.example.ims_backend.common.Gender;
import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.request.candidate.CandidateRequestDTO;
import com.example.ims_backend.dto.response.ResponseData;
import com.example.ims_backend.dto.response.candidate.CandidateExtractDTO;
import com.example.ims_backend.dto.response.candidate.CandidateResponseDTO;
import com.example.ims_backend.dto.response.job.JobDetailsDTO;
import com.example.ims_backend.entity.Candidate;
import com.example.ims_backend.entity.Job;
import com.example.ims_backend.service.CandidateService;
import com.example.ims_backend.service.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@Validated
public class CandidateController {

    private final CandidateService candidateService;
    private final PdfService pdfService;
    private final Validator validator;

    private static final String UPLOAD_DIR = "uploads/";

    @PostConstruct
    public void init() {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory!", e);
            }
        }
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CandidateResponseDTO>>> getCandidates(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) CandidateStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("status"),
                Sort.Order.desc("createdAt")));
        Page<CandidateResponseDTO> result = candidateService.getAllCandidates(pageable, keyword, status);
        return ResponseEntity.ok(new ResponseData<>(result, "Candidates retrieved successfully"));
    }

    @GetMapping("/not-banned")
    public ResponseEntity<ResponseData<List<CandidateResponseDTO>>> getNotBannedCandidates() {
        List<CandidateResponseDTO> result = candidateService.getNotBanndedCandidates();
        return ResponseEntity.ok(new ResponseData<>(result, "Candidates retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id) {
        Optional<CandidateResponseDTO> candidate = candidateService.getCandidateById(id);
        return candidate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCandidate(@RequestPart("candidate") String candidateJson,
                                             @RequestPart("cv") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CandidateRequestDTO candidateRequestDTO;
        try {
            candidateRequestDTO = objectMapper.readValue(candidateJson, CandidateRequestDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to parse candidate JSON");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to parse candidate JSON");
        }

        // Lưu file và đặt đường dẫn vào CandidateRequestDTO
        String fileName = saveFile(file);
        candidateRequestDTO.setCvUrl(UPLOAD_DIR + fileName);

        try {
            // Extract data from PDF
            CandidateExtractDTO extractedData = pdfService.extractDataFromPdf(file);

            // Update candidateRequestDTO with extracted data
            candidateRequestDTO.setFullName(extractedData.getFullName());
            candidateRequestDTO.setDob(extractedData.getDob());
            candidateRequestDTO.setEmail(extractedData.getEmail());
            candidateRequestDTO.setPhoneNumber(extractedData.getPhoneNumber());
            candidateRequestDTO.setAddress(extractedData.getAddress());
            candidateRequestDTO.setGender(extractedData.getGender());
            candidateRequestDTO.setJob(extractedData.getJobs());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to extract job positions from CV");
        }
        System.out.println("Received jobs: " + candidateRequestDTO.getJob());

        // Validate the candidateRequestDTO
        Set<ConstraintViolation<CandidateRequestDTO>> violations = validator.validate(candidateRequestDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CandidateRequestDTO> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        }


        CandidateResponseDTO responseDTO = candidateService.createCandidate(candidateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.append(error.getDefaultMessage()).append("\n");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(
            @PathVariable Long id,
            @RequestPart("candidate") String candidateJson,
            @RequestPart(value = "cv", required = false) MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CandidateRequestDTO candidateRequestDTO;
        try {
            candidateRequestDTO = objectMapper.readValue(candidateJson, CandidateRequestDTO.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to parse candidate JSON");
        }
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file);
            candidateRequestDTO.setCvUrl(UPLOAD_DIR + fileName);
        }

        // Validate the candidateRequestDTO
        Set<ConstraintViolation<CandidateRequestDTO>> violations = validator.validate(candidateRequestDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CandidateRequestDTO> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        }

        // Nếu có tệp được tải lên, lưu nó và cập nhật đường dẫn cvUrl


        CandidateResponseDTO responseDTO = candidateService.updateCandidate(id, candidateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ban")
    public ResponseEntity<CandidateResponseDTO> banCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.banCandidate(id));
    }

    @GetMapping("/{id}/jobs")
    public Set<JobDetailsDTO> getJobsByCandidateId(@PathVariable Long id) {
        return candidateService.getJobsByCandidateId(id);
    }

}
