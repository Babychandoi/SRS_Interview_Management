package com.example.ims_backend.common;

import lombok.Getter;

@Getter
public enum ContractType {
    TRIAL_2_MONTHS("Trial 2 months"),
    TRAINEE_3_MONTHS("Trainee 3 months"),
    ONE_YEAR("1 year"),
    THREE_YEARS_AND_UNLIMITED("3 years and unlimited");

    private final String value;

    ContractType(String value) {
        this.value = value;
    }
}
