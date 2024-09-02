package com.example.ims_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserRegistrationDTO(

        @NotEmpty(message = "User email must not be empty")
        @Email(message = "Invalid email format")
        String email,
        @NotEmpty(message = "User password must not be empty")
        String password,
        @NotEmpty(message = "User role must not be empty")
        String userRole
){ }
