package com.example.ims_backend.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.ims_backend.entity.Email;
import com.example.ims_backend.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(Email email) {
        try {
            if (email.getEmail() == null || email.getEmail().isEmpty()) {
                throw new IllegalArgumentException("To address must not be null or empty");
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email.getEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email.getEmail(), e);
        } catch (IllegalArgumentException e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
