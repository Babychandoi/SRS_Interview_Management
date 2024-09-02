package com.example.ims_backend.repository.specification;

import com.example.ims_backend.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import com.example.ims_backend.common.InterviewStatus;
import com.example.ims_backend.entity.Interview;

public class InterviewSpecification {
    public static Specification<Interview> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("candidate").get("fullName")), likePattern),
                    builder.like(builder.lower(root.get("title")), likePattern));
        };
    }

    public static Specification<Interview> hasStatus(InterviewStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }


    public static Specification<Interview> hasInterviewer(Long id) {
        return (root, query, builder) -> {
            if (id == null) {
                return builder.conjunction();
            }
            Join<Interview, User> join = root.join("interviewers");
            return builder.equal(join.get("id"), id);
        };
    }

}
