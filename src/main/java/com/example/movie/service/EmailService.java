package com.example.movie.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String fromAddress;
    private final String fromName;
    private final String mailProvider;
    private final String brevoApiKey;
    private final String brevoApiUrl;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.base-url:https://movie-manager-production.up.railway.app}") String baseUrl,
            @Value("${app.mail.from:${spring.mail.username:}}") String fromAddress,
            @Value("${app.mail.from-name:Movie Manager}") String fromName,
            @Value("${app.mail.provider:auto}") String mailProvider,
            @Value("${app.mail.brevo.api-key:}") String brevoApiKey,
            @Value("${app.mail.brevo.api-url:https://api.brevo.com/v3/smtp/email}") String brevoApiUrl) {
        this.mailSender = mailSender;
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.mailProvider = mailProvider;
        this.brevoApiKey = brevoApiKey;
        this.brevoApiUrl = brevoApiUrl;
    }

    public void sendResetEmail(String to, String token) {
        String subject = "Password Reset";
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String link = normalizedBaseUrl + "/reset-password?token=" + token;
        String text = "Click this link to reset your password:\n" + link;

        logger.info("Preparing password reset email. to={}, from={}, resetUrl={}, provider={}", to, fromAddress,
                normalizedBaseUrl, mailProvider);

        if (fromAddress == null || fromAddress.isBlank()) {
            throw new IllegalStateException("Mail sender is not configured. Set APP_MAIL_FROM or MAIL_USERNAME.");
        }

        if (useBrevoApi()) {
            sendViaBrevoApi(to, subject, text);
            logger.info("Password reset email sent through Brevo API for {}", to);
            return;
        }

        sendViaSmtp(to, subject, text);
        logger.info("Password reset email submitted to mail server for {}", to);
    }

    private boolean useBrevoApi() {
        if ("brevo-api".equalsIgnoreCase(mailProvider)) {
            return true;
        }
        return "auto".equalsIgnoreCase(mailProvider) && brevoApiKey != null && !brevoApiKey.isBlank();
    }

    private void sendViaSmtp(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    private void sendViaBrevoApi(String to, String subject, String text) {
        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            throw new IllegalStateException("BREVO_API_KEY is required when APP_MAIL_PROVIDER is brevo-api.");
        }

        String requestBody = "{"
                + "\"sender\":{\"name\":\"" + escapeJson(fromName) + "\",\"email\":\"" + escapeJson(fromAddress)
                + "\"},"
                + "\"to\":[{\"email\":\"" + escapeJson(to) + "\"}],"
                + "\"subject\":\"" + escapeJson(subject) + "\","
                + "\"textContent\":\"" + escapeJson(text) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder(URI.create(brevoApiUrl))
                .header("accept", "application/json")
                .header("api-key", brevoApiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Brevo API send failed with status " + response.statusCode() + ": " + response.body());
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to call Brevo API.", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Brevo API call interrupted.", ex);
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}