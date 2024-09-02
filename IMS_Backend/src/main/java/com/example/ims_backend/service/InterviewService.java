package com.example.ims_backend.service;

import com.example.ims_backend.common.InterviewResult;
import com.example.ims_backend.common.InterviewStatus;
import com.example.ims_backend.dto.response.interview.*;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ims_backend.dto.request.interview.InterviewAdd;
import com.example.ims_backend.dto.request.interview.InterviewRequest;

import java.util.List;

public interface InterviewService {
    Page<InterviewResponseDTO> getInterviews(Pageable pageable, String keyword,
                                             Long interviewer, InterviewStatus status);

    InterviewDetailsResponse InterviewDetail(Long id);
    boolean deleteInterview(Long id);
    boolean addInterview(InterviewAdd interviewAdd);
    boolean updateInterview(Long id,InterviewRequest interviewRequest);
    InterviewShowUp InterviewShowUp(Long id);
    InterviewShowAdd InterviewShowAdd();
    boolean sendEmail(Long id) throws MessagingException;
    List<InterviewResponseDTO> candidateInterviews(Long candidateId);
    boolean send(Long id, String node , InterviewResult result);
}
