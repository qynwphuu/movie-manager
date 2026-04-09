package com.example.movie.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.mail.MailException;
import com.example.movie.model.User;
import com.example.movie.repository.UserRepository;
import com.example.movie.service.EmailService;
import java.util.UUID;

@Controller
public class ForgotPasswordController {
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
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);

            try {
                emailService.sendResetEmail(user.getEmail(), token);
            } catch (MailException | IllegalStateException ex) {
                return "redirect:/login?mailError";
            }
        }

        return "redirect:/login?emailSent";
    }
}
