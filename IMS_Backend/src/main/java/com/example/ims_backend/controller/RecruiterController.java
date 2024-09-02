package com.example.ims_backend.controller;

import com.example.ims_backend.entity.User;
import com.example.ims_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllRecruiters() {
        List<User> recruiters = userService.getAllRecruiters();
        return ResponseEntity.ok(recruiters);
    }
}
