package com.example.ims_backend.service;

import com.example.ims_backend.dto.AuthResponseDTO;
import com.example.ims_backend.dto.UserRegistrationDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthResponseDTO getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response);
    Object getAccessTokenUsingRefreshToken(String authorizationHeader);
    AuthResponseDTO registerUser(UserRegistrationDTO userRegistrationDto, HttpServletResponse httpServletResponse);
}
