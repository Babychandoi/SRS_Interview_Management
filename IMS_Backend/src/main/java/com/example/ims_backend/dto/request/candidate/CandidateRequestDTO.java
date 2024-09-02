package com.example.ims_backend.dto.request.candidate;

import com.example.ims_backend.common.AcademicLevel;
import com.example.ims_backend.common.Gender;
import com.example.ims_backend.common.Position;
import com.example.ims_backend.common.Skill;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CandidateRequestDTO {
    @NotBlank(message = "Required field")
    private String fullName;

    @NotNull(message = "Required field doc")
    @Past(message = "Date of Birth must be in the past")
    private LocalDate dob;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Required field")
    private String email;

    @NotBlank(message = "Required field phone number")
    private String phoneNumber;

    @NotBlank(message = "Required field address")
    private String address;

    @NotNull(message = "Required field gender")
    private Gender gender;

    @NotBlank(message = "Required field cvUrl")
    private String cvUrl;

    @NotNull(message = "Required field position")
    private Position position;

    @NotNull(message = "Required field skill")
    private Set<Skill> skills;

    private String note;

    @NotNull(message = "Required field year of experience")
    private Integer yearOfExperience;

    @NotNull(message = "Required field recruiter")
    private UserRequestDTO recruiter;

    @NotNull(message = "Required field highest level")
    private AcademicLevel highestLevel;
    private Set<String> job;
}

