package com.example.ims_backend.repository.specification;

import com.example.ims_backend.common.CandidateStatus;
import com.example.ims_backend.entity.Candidate;
import org.springframework.data.jpa.domain.Specification;

public class CandidateSpecification {
    public static Specification<Candidate> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("fullName")), likePattern),
                    builder.like(builder.lower(root.get("email")), likePattern),
                    builder.like(builder.lower(root.get("phoneNumber")), likePattern),
                    builder.like(builder.lower(root.get("position")), likePattern),
                    builder.like(builder.lower(root.get("recruiter").get("fullName")), likePattern)
            );
        };
    }

    public static Specification<Candidate> hasStatus(CandidateStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Candidate> isNotBanned() {
        return (root, query, builder) -> builder.notEqual(root.get("status"), CandidateStatus.BANNED);
    }
}
