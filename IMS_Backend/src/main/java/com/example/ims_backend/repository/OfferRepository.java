package com.example.ims_backend.repository;

import com.example.ims_backend.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    List<Offer> findAllByCreatedAtBetween(LocalDate from, LocalDate to);
}