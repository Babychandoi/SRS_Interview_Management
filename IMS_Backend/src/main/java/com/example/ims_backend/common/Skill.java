package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum Skill {
    JAVA("Java"),
    NODEJS("NodeJS"),
    DOTNET(".NET"),
    CPP("C++"),
    BUSINESS_ANALYSIS("Business Analysis"),
    COMMUNICATION("Communication");

    private final String value;

    Skill(String value) {
        this.value = value;
    }
}
