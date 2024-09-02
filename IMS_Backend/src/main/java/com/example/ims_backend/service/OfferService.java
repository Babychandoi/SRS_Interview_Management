package com.example.ims_backend.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ims_backend.common.Department;
import com.example.ims_backend.common.OfferStatus;
import com.example.ims_backend.dto.request.offer.OfferRequestDTO;
import com.example.ims_backend.dto.response.offer.OfferDetailsResponseDTO;
import com.example.ims_backend.dto.response.offer.OfferViewResponseDTO;
import com.example.ims_backend.entity.Offer;

import jakarta.servlet.http.HttpServletResponse;

public interface OfferService {
    Page<OfferViewResponseDTO> getOffers(Pageable pageable, String keyword,
            OfferStatus status, Department department);

    Optional<OfferDetailsResponseDTO> getOfferById(Long id);

    Offer saveOffer(OfferRequestDTO offer);

    Boolean changeOfferStatus(Long id, OfferStatus status);

    void generateReport(LocalDate from, LocalDate to, HttpServletResponse response);
}
