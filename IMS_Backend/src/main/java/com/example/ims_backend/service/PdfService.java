package com.example.ims_backend.service;

import com.example.ims_backend.common.Gender;
import com.example.ims_backend.dto.response.candidate.CandidateExtractDTO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.aot.AotServices;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
                PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }

    }
    public Set<String> extractJobsFromText(String text) {
        Set<String> jobs = new HashSet<>();
        Pattern pattern = Pattern.compile("Vị trí ứng tuyển\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String jobLine = matcher.group(1).trim();
            String[] jobArray = jobLine.split(",");
            for (String job : jobArray) {
                jobs.add(job.trim());
            }
        }

        return jobs;
    }
    public String extractFullName(String text) {
        Pattern pattern = Pattern.compile("Họ và tên\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public String extractDob(String text) {
        Pattern pattern = Pattern.compile("Ngày sinh\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public String extractEmail(String text) {
        Pattern pattern = Pattern.compile("Email\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public String extractPhoneNumber(String text) {
        Pattern pattern = Pattern.compile("Số điện thoại\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public String extractAddress(String text) {
        Pattern pattern = Pattern.compile("Địa chỉ\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public String extractGender(String text) {
        Pattern pattern = Pattern.compile("Giới tính\\s*:?\\s*(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
    // New method to extract all personal information and jobs
    public CandidateExtractDTO extractDataFromPdf(MultipartFile file) throws IOException {
        String extractedText = extractTextFromPdf(file);

        Set<String> jobs = extractJobsFromText(extractedText);
        String fullName = extractFullName(extractedText);
        LocalDate dob = parseDate(extractDob(extractedText));
        String email = extractEmail(extractedText);
        String phoneNumber = extractPhoneNumber(extractedText);
        String address = extractAddress(extractedText);
        Gender gender = parseGender(extractGender(extractedText));

        return new CandidateExtractDTO(fullName, dob, email, phoneNumber, address, gender, jobs);
    }
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd MMM yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeException e) {
                // Không làm gì, tiếp tục thử định dạng khác
            }
        }

        return null;
    }

    private Gender parseGender(String genderStr) {
        switch (genderStr.toLowerCase()) {
            case "nam":
                return Gender.MALE;
            case "nữ":
                return Gender.FEMALE;
            case "khác":
                return Gender.OTHERS;
            default:
                return Gender.OTHERS; // Default case
        }
    }

}
