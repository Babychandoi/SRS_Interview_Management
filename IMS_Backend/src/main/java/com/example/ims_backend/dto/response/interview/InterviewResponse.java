package com.example.ims_backend.dto.response.interview;

import com.example.ims_backend.dto.InterviewDTO;
import com.example.ims_backend.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class InterviewResponse extends InterviewDTO {
    private String positionLevel;
    private String candidateName;
    public InterviewResponse() {
    };
    public InterviewResponse(Long id, String title, String candidateName, String scheduleTime, String fromTime,
                             List<UserDTO> interviewers,
                             String toTime, String result, String status, String positionLevel) {
        super(id, title,  scheduleTime, fromTime, toTime, result, status, interviewers);
        this.candidateName = candidateName;
        this.positionLevel = positionLevel;
    }

}
