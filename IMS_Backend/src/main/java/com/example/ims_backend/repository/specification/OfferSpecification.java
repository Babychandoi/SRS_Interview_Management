package com.example.ims_backend.repository.specification;

import com.example.ims_backend.common.Department;
import com.example.ims_backend.common.OfferStatus;
import com.example.ims_backend.entity.Offer;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification {
    public static Specification<Offer> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("candidate").get("fullName")), likePattern),
                    builder.like(builder.lower(root.get("note")), likePattern),
                    builder.like(builder.lower(root.get("candidate").get("email")), likePattern),
                    builder.like(builder.lower(root.get("approver").get("fullName")), likePattern)
            );
        };
    }

    public static Specification<Offer> hasStatus(OfferStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Offer> hasDepartment(Department department) {
        return (root, query, builder) -> {
            if (department == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("department"), department);
        };
    }
}
