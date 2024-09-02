package com.example.ims_backend.controller;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ims_backend.common.Department;
import com.example.ims_backend.common.OfferStatus;
import com.example.ims_backend.dto.request.offer.OfferExportRequestDTO;
import com.example.ims_backend.dto.request.offer.OfferRequestDTO;
import com.example.ims_backend.dto.response.ResponseData;
import com.example.ims_backend.dto.response.offer.OfferDetailsResponseDTO;
import com.example.ims_backend.dto.response.offer.OfferViewResponseDTO;
import com.example.ims_backend.entity.Offer;
import com.example.ims_backend.service.OfferService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/offers")
@CrossOrigin(origins = { "http://localhost:4200" })
public class OfferController {
    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<OfferViewResponseDTO>>> getOffers(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OfferStatus status,
            @RequestParam(required = false) Department department) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OfferViewResponseDTO> offers = offerService.getOffers(pageable, keyword, status, department);
        return ResponseEntity.ok(new ResponseData<>(offers, "Offers retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<OfferDetailsResponseDTO>> getOfferById(@PathVariable Long id) {
        Optional<OfferDetailsResponseDTO> offer = offerService.getOfferById(id);
        return offer.map(offerDetailsResponseDTO -> ResponseEntity
                .ok(new ResponseData<>(offerDetailsResponseDTO, "Offer retrieved successfully")))
                .orElseGet(() -> new ResponseEntity<>(new ResponseData<>(null, "Offer not found"),
                        HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_RECRUITER')")
    public ResponseEntity<ResponseData<Offer>> createOffer(@RequestBody OfferRequestDTO offer) {
        try {
            Offer o = offerService.saveOffer(offer);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseData<>(o, "Offer created successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseData<Offer>> updateOffer(@RequestBody OfferRequestDTO offer) {
        try {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new ResponseData<>(offerService.saveOffer(offer), "Offer updated successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/export")
    public void exportOffers(@RequestBody OfferExportRequestDTO request,
            HttpServletResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String from = request.getFrom().format(formatter);
        String to = request.getTo().format(formatter);

        System.out.println("Exporting offers from " + from + " to " + to);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=OfferList-" +
                from + "-" +
                to + ".xlsx";
        response.setHeader(headerKey, headerValue);
        offerService.generateReport(request.getFrom(), request.getTo(), response);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseData<Boolean>> cancelOffer(@PathVariable Long id) {
        try {
            boolean isCancelled = offerService.changeOfferStatus(id, OfferStatus.CANCELLED);
            if (isCancelled) {
                return ResponseEntity.ok(new ResponseData<>(true, "Offer cancelled successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ResponseData<>(false, "Offer not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData<>(null, e.getMessage()));
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<ResponseData<Boolean>> approveOffer(@PathVariable Long id, @RequestParam Boolean flag) {
        try {
            boolean isApproved = offerService.changeOfferStatus(id, flag ? OfferStatus.APPROVED : OfferStatus.REJECTED);
            if (isApproved) {
                return ResponseEntity.ok(new ResponseData<>(true, "Successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ResponseData<>(false, "Offer not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData<>(null, e.getMessage()));
        }
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<ResponseData<Boolean>> acceptOffer(@PathVariable Long id, @RequestParam Boolean flag) {
        try {
            boolean isAccepted = offerService.changeOfferStatus(id, flag ? OfferStatus.ACCEPTED : OfferStatus.DECLINED);
            if (isAccepted) {
                return ResponseEntity.ok(new ResponseData<>(true, "Successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ResponseData<>(false, "Offer not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData<>(null, e.getMessage()));
        }
    }

    @PutMapping("/mark-as-sent/{id}")
    public ResponseEntity<ResponseData<Boolean>> markAsSentOffer(@PathVariable Long id) {
        try {
            boolean isUpdated = offerService.changeOfferStatus(id, OfferStatus.WAITING_RESPONSE);
            if (isUpdated) {
                return ResponseEntity.ok(new ResponseData<>(true, "Successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ResponseData<>(false, "Offer not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData<>(null, e.getMessage()));
        }
    }

}
