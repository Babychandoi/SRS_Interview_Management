package com.example.ims_backend.controller;

import com.example.ims_backend.dto.response.ResponseData;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseData<>(users, "Success"));
    }

    @GetMapping("/recruiters")
    public ResponseEntity<List<User>> getAllRecruiters() {
        List<User> recruiters = userService.getAllRecruiters();
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/interviewers")
    public ResponseEntity<ResponseData<List<User>>> getAllInterviewers() {
        List<User> interviewers = userService.getAllInterviewers();
        return ResponseEntity.ok(new ResponseData<>(interviewers, "Success"));
    }



}
