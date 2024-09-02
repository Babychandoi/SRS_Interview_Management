package com.example.ims_backend.dto.response.interview;

import java.util.List;

import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.JobDTO;
import com.example.ims_backend.dto.UserDTO;

import lombok.Data;

@Data
public class InterviewShowUp extends InterviewDetailsResponse {
    private List<JobDTO> job;
    private List<CandidateDTO> candidate;
    private List<UserDTO> Userinterviewers;
    private String jobTitle;

    public InterviewShowUp() {
    }

    public InterviewShowUp(Long id, String title, String candidateName, String scheduleTime, String fromTime,
            List<UserDTO> interviewers, String toTime, String result, String status, String location, String meetingId,
            String note, String recruiter, String positionLevel, List<JobDTO> job, List<CandidateDTO> candidate,
            List<UserDTO> interviewers1, String jobTitle) {
        super(id, title, candidateName, scheduleTime, fromTime, interviewers, toTime, result, status, location,
                meetingId, note, recruiter, positionLevel);
        this.job = job;
        this.candidate = candidate;
        this.jobTitle = jobTitle;
        this.Userinterviewers = interviewers1;
    }
}
