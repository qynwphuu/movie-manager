package com.example.movie.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.mail.MailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.movie.model.User;
import com.example.movie.repository.UserRepository;
import com.example.movie.service.EmailService;
import java.util.UUID;

@Controller
public class ForgotPasswordController {
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email) {
        String normalizedEmail = email == null ? "" : email.trim();
        logger.info("Forgot password requested for email: {}", normalizedEmail);

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);

            try {
                emailService.sendResetEmail(user.getEmail(), token);
                logger.info("Reset email sent successfully to: {}", user.getEmail());
            } catch (MailException | IllegalStateException ex) {
                logger.error("Failed to send reset email to {}: {}", user.getEmail(), ex.getMessage(), ex);
                return "redirect:/login?mailError";
            }
        } else {
            logger.warn("Forgot password requested for unknown email: {}", normalizedEmail);
        }

        return "redirect:/login?emailSent";
    }
}
