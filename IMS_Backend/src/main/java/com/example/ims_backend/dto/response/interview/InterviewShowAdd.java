package com.example.ims_backend.dto.response.interview;

import java.util.List;

import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.JobDTO;
import com.example.ims_backend.dto.UserDTO;

import lombok.Data;

@Data
public class InterviewShowAdd {
    private List<JobDTO> job;
    private List<CandidateDTO> candidate;
    private List<UserDTO> Userinterviewers;

    public InterviewShowAdd() {
    };

    public InterviewShowAdd(List<JobDTO> job, List<CandidateDTO> candidate, List<UserDTO> interviewers1) {
        this.job = job;
        this.candidate = candidate;
        this.Userinterviewers = interviewers1;
    }
}
