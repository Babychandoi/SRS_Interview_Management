package com.example.ims_backend.dto.response.offer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferViewResponseDTO {
    private Long id;
    private String candidateName;
    private String email;
    private String approver;
    private String department;
    private String note;
    private String status;
}
