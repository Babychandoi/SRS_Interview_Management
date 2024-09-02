package com.example.ims_backend.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobDTO {
    private Long id;
    private String title;
    private String recruiter;
    private List <String> position;

    public JobDTO() {
        position = new ArrayList<>();
    }
}
