package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum PositionLevel {
    FRESHER("Fresher"),
    JUNIOR("Junior"),
    SENIOR("Senior"),
    LEADER("Leader"),
    MANAGER("Manager"),
    VICE_HEAD("Vice Head");

    private final String value;

    PositionLevel(String value) {
        this.value = value;
    }
}
