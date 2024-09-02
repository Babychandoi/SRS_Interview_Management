package com.example.ims_backend.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.ims_backend.entity.SecurityUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final JwtTokenUtils jwtTokenUtils;

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    public SecurityUser getAuthenticatedUser() {
        String username = getAuthenticatedUsername();
        return (SecurityUser) jwtTokenUtils.userDetails(username);
    }

}
