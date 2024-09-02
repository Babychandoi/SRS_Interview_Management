package com.example.ims_backend.service;

import com.example.ims_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User findById(Long id);
    User findByEmail(String email);
    List<User> getAllRecruiters() ;
    List<User> getAllInterviewers();
}

