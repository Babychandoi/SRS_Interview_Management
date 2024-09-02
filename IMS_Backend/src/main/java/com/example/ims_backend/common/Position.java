package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum Position {
    BACKEND_DEVELOPER("Backend Developer"),
    FRONTEND_DEVELOPER("Frontend Developer"),
    BUSINESS_ANALYST("Business Analyst"),
    TESTER("Tester"),
    HR("HR"),
    PROJECT_MANAGER("Project Manager"),
    NOT_AVAILABLE("Not Available");

    private final String value;

    Position(String value) {
        this.value = value;
    }
}
