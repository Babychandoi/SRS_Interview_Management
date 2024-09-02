package com.example.ims_backend.dto.response.interview;

import java.util.List;

import com.example.ims_backend.dto.InterviewDTO;
import com.example.ims_backend.dto.UserDTO;

import lombok.Data;

@Data
public class InterviewDetailsResponse extends InterviewDTO {
    private String candidateName;
    private String location;
    private String meetingId;
    private String note;
    private String recruiter;
    private String positionLevel;

    public InterviewDetailsResponse() {
    };

    public InterviewDetailsResponse(Long id, String title, String candidateName, String scheduleTime, String fromTime,
            List<UserDTO> interviewers,
            String toTime, String result, String status, String location, String meetingId,
            String note, String recruiter, String positionLevel) {
        super(id, title, scheduleTime, fromTime, toTime, result, status, interviewers);
        this.location = location;
        this.meetingId = meetingId;
        this.note = note;
        this.candidateName = candidateName;
        this.recruiter = recruiter;
        this.positionLevel = positionLevel;
    }
}
