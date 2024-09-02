package com.example.ims_backend.controller;

import com.example.ims_backend.common.InterviewResult;
import com.example.ims_backend.common.InterviewStatus;
import com.example.ims_backend.dto.response.interview.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ims_backend.dto.request.interview.DataSearch;
import com.example.ims_backend.dto.request.interview.InterviewAdd;
import com.example.ims_backend.dto.request.interview.InterviewRequest;
import com.example.ims_backend.dto.response.ResponseData;
import com.example.ims_backend.service.EmailService;
import com.example.ims_backend.service.InterviewService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    private final EmailService emailService;

    @GetMapping("/view")
    public ResponseEntity<ResponseData<Page<InterviewResponseDTO>>> getInterviews(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long interviewerId,
            @RequestParam(required = false) InterviewStatus status) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "interviewDate"));
        Page<InterviewResponseDTO> interviews = interviewService.getInterviews(pageable, keyword,
                interviewerId, status);
        return ResponseEntity.ok(new ResponseData<>(interviews, "Interviews retrieved successfully"));
    }


    @DeleteMapping("/{id}")
    public boolean deleteInterview(@PathVariable Long id) {
        return interviewService.deleteInterview(id);
    }

    @GetMapping("/details/{id}")
    public InterviewDetailsResponse getInterviewDetail(@PathVariable Long id) {
        return interviewService.InterviewDetail(id);
    }

    @GetMapping("/showup/{id}")
    public InterviewShowUp getInterviewShowUp(@PathVariable Long id) {
        return interviewService.InterviewShowUp(id);
    }

    @GetMapping("/showadd")
    public InterviewShowAdd getInterviewShowAdd() {
        return interviewService.InterviewShowAdd();
    }

    @PutMapping("/{id}")
    public boolean updateInterview(@PathVariable Long id, @RequestBody InterviewRequest interviewRequest) {
        return interviewService.updateInterview(id, interviewRequest);
    }

    @PostMapping
    public boolean addInterview(@RequestBody InterviewAdd interviewAdd) {
        return interviewService.addInterview(interviewAdd);
    }

    @GetMapping("/candidate/{id}")
    public ResponseEntity<ResponseData<List<InterviewResponseDTO>>> getCandidateInterviews(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseData<>(interviewService.candidateInterviews(id),
                "Interviews retrieved successfully"));
    }


    @GetMapping("/sendEmail/{id}")
    public boolean sendEmail(@PathVariable Long id) throws MessagingException {
        return interviewService.sendEmail(id);
    }
    @PostMapping("/sendEmail/{id}")
    public boolean send(@PathVariable Long id,
                        @RequestParam("node") String node,
                        @RequestParam("result") InterviewResult result
    ) throws MessagingException {
        return interviewService.send(id,node,result);
    }
}
