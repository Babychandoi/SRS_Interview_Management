package com.example.ims_backend.dto.response.candidate;

import com.example.ims_backend.common.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
@Data
public class CandidateExtractDTO {
    private String fullName;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private Set<String> jobs;

    public CandidateExtractDTO(String fullName, LocalDate dob, String email, String phoneNumber, String address, Gender gender, Set<String> jobs) {
        this.fullName = fullName;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.jobs = jobs;
    }
}
