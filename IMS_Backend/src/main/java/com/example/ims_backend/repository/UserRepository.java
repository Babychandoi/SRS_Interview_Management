package com.example.ims_backend.repository;

import java.util.List;
import java.util.Optional;

import com.example.ims_backend.common.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ims_backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    List<User> findByRole(Role role);

    @Query(value = "SELECT DISTINCT u.* FROM user u " +
            "INNER JOIN interview_interviewers ui ON u.id = ui.interviewers_id " +
            "INNER JOIN interview i ON ui.interview_id = i.id", nativeQuery = true)
    List<User> findAllUsersWithInterviews();
}
