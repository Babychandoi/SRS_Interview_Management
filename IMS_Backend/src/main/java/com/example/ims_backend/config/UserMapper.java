package com.example.ims_backend.config;

import com.example.ims_backend.common.Role;
import com.example.ims_backend.dto.UserRegistrationDTO;
import com.example.ims_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    public User convertToEntity(UserRegistrationDTO userRegistrationDto) {
        User userInfoEntity = new User();
        userInfoEntity.setEmail(userRegistrationDto.email());
        userInfoEntity.setRole(Role.valueOf("ROLE_"+userRegistrationDto.userRole()));
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.password()));
        return userInfoEntity;
    }
}
