package com.example.ims_backend.controller;

import com.example.ims_backend.dto.response.candidate.CandidateExtractDTO;
import com.example.ims_backend.service.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RestController

@RequestMapping("/api/files")
public class FileController {

    private final ResourceLoader resourceLoader;

    private final PdfService pdfService;

    public FileController(ResourceLoader resourceLoader, PdfService pdfService) {
        this.resourceLoader = resourceLoader;
        this.pdfService = pdfService;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        try {
            // Tạo đường dẫn tệp từ tên tệp
            Path filePath = Paths.get("uploads").resolve(filename).normalize();

            // Tạo đối tượng Resource từ đường dẫn tệp
            Resource resource = new FileSystemResource(filePath.toFile());

            // Kiểm tra tệp có tồn tại và có thể đọc được không
            if (resource.exists() && resource.isReadable()) {
                // Trả về tệp PDF với đúng Content-Type
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/extract")
    public ResponseEntity<CandidateExtractDTO> extractTextFromFile(@RequestParam("file") MultipartFile file) {
        try {
            CandidateExtractDTO extractedData = pdfService.extractDataFromPdf(file);
            return ResponseEntity.ok(extractedData);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
