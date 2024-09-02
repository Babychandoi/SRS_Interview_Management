package com.example.ims_backend.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.InterviewDTO;
import com.example.ims_backend.dto.UserDTO;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.ims_backend.common.Department;
import com.example.ims_backend.common.InterviewResult;
import com.example.ims_backend.common.OfferStatus;
import com.example.ims_backend.config.SecurityUtils;
import com.example.ims_backend.dto.request.offer.OfferRequestDTO;
import com.example.ims_backend.dto.response.offer.OfferDetailsResponseDTO;
import com.example.ims_backend.dto.response.offer.OfferViewResponseDTO;
import com.example.ims_backend.entity.Email;
import com.example.ims_backend.entity.Offer;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.repository.OfferRepository;
import com.example.ims_backend.repository.specification.OfferSpecification;
import com.example.ims_backend.service.EmailService;
import com.example.ims_backend.service.OfferService;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final SecurityUtils securityUtils;
    private final EmailService emailService;

    @Override
    public Page<OfferViewResponseDTO> getOffers(Pageable pageable, String keyword,
            OfferStatus status, Department department) {
        log.info("Fetching offers with filters - keyword: {}, status: {}, department: {}", keyword, status, department);

        Specification<Offer> spec = Specification.where(OfferSpecification.hasKeyword(keyword))
                .and(OfferSpecification.hasStatus(status))
                .and(OfferSpecification.hasDepartment(department));

        Page<Offer> offers = offerRepository.findAll(spec, pageable);
        List<OfferViewResponseDTO> offerViewResponsDTOS = offers.stream().map(o -> {
            OfferViewResponseDTO offerViewResponseDTO = new OfferViewResponseDTO();
            offerViewResponseDTO.setId(o.getId());
            if (o.getCandidate() != null) {
                offerViewResponseDTO.setCandidateName(o.getCandidate().getFullName());
                offerViewResponseDTO.setEmail(o.getCandidate().getEmail());
            }
            if (o.getApprover() != null) {
                offerViewResponseDTO.setApprover(o.getApprover().getFullName());
            }
            if (o.getDepartment() != null) {
                offerViewResponseDTO.setDepartment(o.getDepartment().getValue());
            }
            offerViewResponseDTO.setNote(o.getNote());
            if (o.getStatus() != null) {
                offerViewResponseDTO.setStatus(o.getStatus().getValue());
            }
            return offerViewResponseDTO;
        }).collect(Collectors.toList());
        return new PageImpl<>(offerViewResponsDTOS, pageable, offers.getTotalElements());
    }

    @Override
    public Optional<OfferDetailsResponseDTO> getOfferById(Long id) {
        log.info("Fetching offer by id: {}", id);
        Optional<Offer> offer = offerRepository.findById(id);
        if (offer.isEmpty()) {
            return Optional.empty();
        }
        Offer o = offer.get();
        OfferDetailsResponseDTO offerDetailsResponseDTO = new OfferDetailsResponseDTO();
        offerDetailsResponseDTO.setId(o.getId());
        if (o.getCandidate() != null) {
            CandidateDTO candidateDTO = new CandidateDTO(o.getCandidate().getId(), o.getCandidate().getFullName(),
                    o.getCandidate().getEmail(), null);
            offerDetailsResponseDTO.setCandidate(candidateDTO);
        }
        offerDetailsResponseDTO.setPosition(o.getPosition());
        offerDetailsResponseDTO.setPositionName(o.getPosition().getValue());
        if (o.getApprover() != null) {
            UserDTO userDTO = new UserDTO(o.getApprover().getId(), o.getApprover().getFullName(), o.getApprover().getEmail(),
                    o.getApprover().getRole());
            offerDetailsResponseDTO.setApprover(userDTO);
        }
        if (o.getInterview() != null) {
            InterviewDTO interviewDTO = new InterviewDTO();
            interviewDTO.setId(o.getInterview().getId());
            interviewDTO.setTitle(o.getInterview().getTitle());
            interviewDTO.setScheduleTime(o.getInterview().getInterviewDate().toString());
            interviewDTO.setFromTime(o.getInterview().getFromTime().toString());
            interviewDTO.setToTime(o.getInterview().getToTime().toString());
            interviewDTO.setResult(o.getInterview().getResult().name());
            interviewDTO.setStatus(o.getInterview().getStatus().name());
            interviewDTO.setLocation(o.getInterview().getLocation());
            interviewDTO.setMeetingId(o.getInterview().getMeetingId());
            interviewDTO.setNote(o.getInterview().getNote());
            offerDetailsResponseDTO.setInterview(interviewDTO);
        }
        offerDetailsResponseDTO.setContractPeriodFrom(o.getContractPeriodFrom());
        offerDetailsResponseDTO.setContractPeriodTo(o.getContractPeriodTo());
        offerDetailsResponseDTO.setContractType(o.getContractType());
        offerDetailsResponseDTO.setContractTypeName(o.getContractType().getValue());
        offerDetailsResponseDTO.setLevel(o.getLevel());
        offerDetailsResponseDTO.setLevelName(o.getLevel().getValue());
        offerDetailsResponseDTO.setStatus(o.getStatus());
        offerDetailsResponseDTO.setStatusName(o.getStatus().getValue());
        offerDetailsResponseDTO.setDepartment(o.getDepartment());
        offerDetailsResponseDTO.setDepartmentName(o.getDepartment().getValue());
        if (o.getRecruiter() != null) {
            offerDetailsResponseDTO.setRecruiter(o.getRecruiter());
        }
        offerDetailsResponseDTO.setDueDate(o.getDueDate());
        offerDetailsResponseDTO.setBasicSalary(o.getBasicSalary());
        offerDetailsResponseDTO.setNote(o.getNote());
        offerDetailsResponseDTO.setCreatedAt(o.getCreatedAt());
        offerDetailsResponseDTO.setLastUpdatedBy(o.getLastUpdatedBy());
        return Optional.of(offerDetailsResponseDTO);
    }

    @Override
    @Transactional
    public Offer saveOffer(OfferRequestDTO offerRequestDTO) {
        User user = securityUtils.getAuthenticatedUser().getUser();
        boolean isUpdate = offerRequestDTO.getId() != null;
        log.info("Creating offer: {}", offerRequestDTO);
        Offer offer;
        if (isUpdate) {
            offer = offerRepository.findById(offerRequestDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Offer not found"));
            offer.setStatus(offerRequestDTO.getStatus());
            offer.setCreatedAt(offerRequestDTO.getCreatedAt());
            if (offerRequestDTO.getDueDate().isBefore(offer.getCreatedAt())) {
                throw new RuntimeException("Due date must be after created date");
            }
        } else {
            offer = new Offer();
            if (offerRequestDTO.getDueDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Due date must be in the future");
            }
            offer.setRecruiter(user);
            offer.setStatus(OfferStatus.WAITING_APPROVAL);
        }
        if (offerRequestDTO.getApprover() != null) {
            offer.setApprover(offerRequestDTO.getApprover());
        }
        offer.setPosition(offerRequestDTO.getPosition());
        if (offerRequestDTO.getCandidate() != null) {
            offer.setCandidate(offerRequestDTO.getCandidate());
        }
        if (offerRequestDTO.getInterview() != null) {
            if (offerRequestDTO.getInterview().getResult() == InterviewResult.FAILED) {
                throw new RuntimeException("Cannot create offer for failed interview");
            }
            offer.setInterview(offerRequestDTO.getInterview());
        }
        if (offerRequestDTO.getContractPeriodTo().isBefore(offerRequestDTO.getContractPeriodFrom())) {
            throw new RuntimeException("Contract period to must be after contract period from");
        }
        offer.setLastUpdatedBy(user);
        offer.setContractPeriodFrom(offerRequestDTO.getContractPeriodFrom());
        offer.setContractPeriodTo(offerRequestDTO.getContractPeriodTo());
        offer.setContractType(offerRequestDTO.getContractType());
        offer.setLevel(offerRequestDTO.getLevel());
        offer.setDueDate(offerRequestDTO.getDueDate());
        offer.setDepartment(offerRequestDTO.getDepartment());
        if (offerRequestDTO.getRecruiter() != null) {
            offer.setRecruiter(offerRequestDTO.getRecruiter());
        }
        offer.setBasicSalary(offerRequestDTO.getBasicSalary());
        offer.setNote(offerRequestDTO.getNote());
        return offerRepository.save(offer);
    }

    @Override
    public Boolean changeOfferStatus(Long id, OfferStatus offerStatus) {
        log.info("Cancelling offer with id: {}", id);
        Optional<Offer> o = offerRepository.findById(id);
        if (o.isEmpty()) {
            return false;
        }
        Offer offer = o.get();
        offer.setStatus(offerStatus);
        if (offerStatus == OfferStatus.REJECTED || offerStatus == OfferStatus.CANCELLED) {
            Email email = new Email();
            String jobTitle = offer.getInterview().getJob().getTitle();
            email.setSubject("Offer for " + jobTitle + " rejected");
            email.setBody("I am sorry to inform you that" +
                    " you have been rejected the offer for the position "
                    + jobTitle + " at " + offer.getDepartment().getValue());
            email.setEmail(offer.getCandidate().getEmail());
            emailService.sendEmail(email);

        } else if (offerStatus == OfferStatus.WAITING_RESPONSE) {
            Email email = new Email();
            String jobTitle = offer.getInterview().getJob().getTitle();
            email.setSubject("Offer for " + jobTitle);
            email.setBody("You have pass. " +
                    "You have an offer for the position " + jobTitle + " at " + offer.getDepartment().getValue() +
                    ", please respond before " + offer.getDueDate());
            email.setEmail(offer.getCandidate().getEmail());
            emailService.sendEmail(email);
        }
        offerRepository.save(offer);
        return true;
    }

    @Override
    public void generateReport(LocalDate from, LocalDate to, HttpServletResponse response) {
        log.info("Generating report from {} to {}", from, to);
        List<Offer> offers = offerRepository.findAllByCreatedAtBetween(from, to);
        if (offers.isEmpty()) {
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Offer-List");

        // Set column widths
        sheet.setColumnWidth(0, 8 * 256);
        sheet.setColumnWidth(1, 28 * 256);
        sheet.setColumnWidth(2, 38 * 256);
        sheet.setColumnWidth(3, 23 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 72 * 256);
        sheet.setColumnWidth(6, 23 * 256);

        // Create a font and style for the header
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create header
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.getCell(0).setCellStyle(headerCellStyle);
        header.createCell(1).setCellValue("Candidate name");
        header.getCell(1).setCellStyle(headerCellStyle);
        header.createCell(2).setCellValue("Email");
        header.getCell(2).setCellStyle(headerCellStyle);
        header.createCell(3).setCellValue("Approver");
        header.getCell(3).setCellStyle(headerCellStyle);
        header.createCell(4).setCellValue("Department");
        header.getCell(4).setCellStyle(headerCellStyle);
        header.createCell(5).setCellValue("Note");
        header.getCell(5).setCellStyle(headerCellStyle);
        header.createCell(6).setCellValue("Status");
        header.getCell(6).setCellStyle(headerCellStyle);

        // Create a font and style for the data
        HSSFFont dataFont = workbook.createFont();
        dataFont.setBold(false);
        dataFont.setFontHeightInPoints((short) 12);
        dataFont.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setFont(dataFont);

        int rowNum = 1;
        for (Offer o : offers) {
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(o.getId());
            row.getCell(0).setCellStyle(dataCellStyle);
            if (o.getCandidate() != null) {
                row.createCell(1).setCellValue(o.getCandidate().getFullName());
                row.getCell(1).setCellStyle(dataCellStyle);
                row.createCell(2).setCellValue(o.getCandidate().getEmail());
                row.getCell(2).setCellStyle(dataCellStyle);
            }
            if (o.getApprover() != null) {
                row.createCell(3).setCellValue(o.getApprover().getFullName());
                row.getCell(3).setCellStyle(dataCellStyle);
            }
            row.createCell(4).setCellValue(o.getDepartment().getValue());
            row.getCell(4).setCellStyle(dataCellStyle);
            row.createCell(5).setCellValue(o.getNote());
            row.getCell(5).setCellStyle(dataCellStyle);
            row.createCell(6).setCellValue(o.getStatus().getValue());
            row.getCell(6).setCellStyle(dataCellStyle);
        }

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
