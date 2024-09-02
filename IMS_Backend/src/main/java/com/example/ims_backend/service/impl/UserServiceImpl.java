package com.example.ims_backend.service.impl;

import com.example.ims_backend.common.Role;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.repository.UserRepository;
import com.example.ims_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public List<User> getAllRecruiters() {
        return userRepository.findByRole(Role.ROLE_RECRUITER);
    }

    @Override
    public List<User> getAllInterviewers() {
        return userRepository.findAllUsersWithInterviews();
    }

}
