package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum Department {
    IT("IT"),
    HR("HR"),
    FINANCE("Finance"),
    COMMUNICATION("Communication"),
    MARKETING("Marketing"),
    ACCOUNTING("Accounting");

    private final String value;

    Department(String value) {
        this.value = value;
    }
}
