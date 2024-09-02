package com.example.ims_backend.dto.request.interview;

import lombok.Data;

@Data
public class DataSearch {
    private String key;
    private String interviewerName;
    private String status;
    public DataSearch() {
    }
    public DataSearch(String key, String interviewerName, String status) {
        this.key = key;
        this.interviewerName = interviewerName;
        this.status = status;
    }
}
