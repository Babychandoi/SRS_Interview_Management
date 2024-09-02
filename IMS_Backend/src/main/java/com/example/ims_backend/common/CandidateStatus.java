package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum CandidateStatus {
    OPEN("Open", 4),
    WAITING_INTERVIEW("Waiting for interview", 1),
    CANCELLED_INTERVIEW("Cancelled interview", 12),
    PASS_INTERVIEW("Passed interview", 5),
    FAILED_INTERVIEW("Failed interview", 11),
    WAITING_APPROVAL("Waiting for approval", 2),
    APPROVED_OFFER("Approved offer", 6),
    REJECTED_OFFER("Rejected offer", 7),
    WAITING_RESPONSE("Waiting for response", 3),
    ACCEPTED_OFFER("Accepted offer", 8),
    DECLINED_OFFER("Declined offer", 9),
    CANCELLED_OFFER("Cancelled offer", 10),
    BANNED("Banned", 13);

    private final int order;
    private final String value;

    CandidateStatus(String value, int order) {
        this.value = value;
        this.order = order;
    }

}
