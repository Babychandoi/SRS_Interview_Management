package com.example.ims_backend.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    @NotBlank(message = "Email cannot be blanked")
    private String email;
    @NotBlank(message = "Subject cannot be blanked")
    private String subject;
    @NotBlank(message = "Body cannot be blanked")
    private String body;

}
