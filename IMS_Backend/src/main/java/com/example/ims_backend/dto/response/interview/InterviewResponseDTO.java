package com.example.ims_backend.dto.response.interview;

import com.example.ims_backend.dto.InterviewDTO;
import com.example.ims_backend.dto.JobDTO;
import com.example.ims_backend.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class InterviewResponseDTO extends InterviewDTO {
    private String positionLevel;
    private String candidateName;
    private UserDTO recruiter;
    private JobDTO job;

    public InterviewResponseDTO() {
    };

}
