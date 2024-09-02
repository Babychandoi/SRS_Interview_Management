package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum OfferStatus {
    WAITING_APPROVAL("Waiting for approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    WAITING_RESPONSE("Waiting for response"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    CANCELLED("Cancelled");

    private final String value;

    OfferStatus(String value) {
        this.value = value;
    }

}
