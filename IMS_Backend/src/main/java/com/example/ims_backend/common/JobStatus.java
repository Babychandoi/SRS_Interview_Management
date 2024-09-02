package com.example.ims_backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum JobStatus {
    DRAFT("Draft"),
    OPEN("Open"),
    CLOSE("Close");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }
}
