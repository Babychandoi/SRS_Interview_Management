package com.example.ims_backend.repository.specification;

import com.example.ims_backend.common.JobStatus;
import com.example.ims_backend.entity.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase().trim() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("title")), likePattern),
                    builder.like(builder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Job> hasStatus(JobStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }
}
