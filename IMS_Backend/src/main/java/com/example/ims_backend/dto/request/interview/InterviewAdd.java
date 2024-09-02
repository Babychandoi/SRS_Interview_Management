package com.example.ims_backend.dto.request.interview;

import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.JobDTO;
import com.example.ims_backend.dto.UserDTO;
import com.example.ims_backend.entity.Candidate;
import lombok.Data;

import java.util.List;
@Data
public class InterviewAdd {
    private String title;
    private String candidateName;
    private String scheduleTime;
    private String fromTime;
    private String toTime;
    private String location;
    private String meetingId;
    private String note;
    private String status;
    private String result;
    private String recruiter;
    private List<JobDTO> job;
    private List<CandidateDTO> candidate;
    private List<UserDTO> interviewers;
    private List<UserDTO> userinterviewers;

    public InterviewAdd() {
    };
    public InterviewAdd(String title, String candidateName, String scheduleTime, String fromTime, String toTime, String location, String meetingId, String note, String status, String result, String recruiter, List<JobDTO> job, List<CandidateDTO> candidate, List<UserDTO> interviewers, List<UserDTO> userinterviewers) {
        this.title = title;
        this.candidateName = candidateName;
        this.scheduleTime = scheduleTime;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.location = location;
        this.meetingId = meetingId;
        this.note = note;
        this.status = status;
        this.result = result;
        this.recruiter = recruiter;
        this.job = job;
        this.candidate = candidate;
        this.interviewers = interviewers;
        this.userinterviewers = userinterviewers;
    }

}
