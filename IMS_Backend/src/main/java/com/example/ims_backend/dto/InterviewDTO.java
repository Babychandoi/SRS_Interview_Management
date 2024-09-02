package com.example.ims_backend.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InterviewDTO {
    private Long id;
    private String title;

    private List<UserDTO> interviewers = new ArrayList<>();
    private String scheduleTime;
    private String fromTime;
    private String toTime;
    private String result;
    private String status;
    private String location;
    private String meetingId;
    private String note;

    public InterviewDTO() {
    }

    public InterviewDTO(Long id, String title, String scheduleTime, String fromTime, String toTime, String result,
            String status, List<UserDTO> interviewers) {
        this.id = id;
        this.title = title;
        this.interviewers = interviewers;
        this.scheduleTime = scheduleTime;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.result = result;
        this.status = status;
    }
}
