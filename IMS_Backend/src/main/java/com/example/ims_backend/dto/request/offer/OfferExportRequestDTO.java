package com.example.ims_backend.dto.request.offer;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class OfferExportRequestDTO {
    LocalDate from;
    LocalDate to;
}
