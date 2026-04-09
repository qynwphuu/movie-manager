package com.example.movie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String baseUrl;
    private final String fromAddress;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.base-url:https://movie-manager-production.up.railway.app}") String baseUrl,
            @Value("${spring.mail.username:}") String fromAddress) {
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
        this.fromAddress = fromAddress;
    }

    public void sendResetEmail(String to, String token) {
        String subject = "Password Reset";
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String link = normalizedBaseUrl + "/reset-password?token=" + token;
        logger.info("Preparing password reset email. to={}, from={}, resetUrl={}", to, fromAddress, normalizedBaseUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        if (fromAddress == null || fromAddress.isBlank()) {
            throw new IllegalStateException("Mail sender is not configured. Set MAIL_USERNAME.");
        }
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Click this link to reset your password:\n" + link);

        mailSender.send(message);
        logger.info("Password reset email submitted to mail server for {}", to);
    }
}