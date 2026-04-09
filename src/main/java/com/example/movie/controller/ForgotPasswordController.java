package com.example.movie.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);

            emailService.sendResetEmail(user.getEmail(), token);
        }

        return "redirect:/login?emailSent";
    }
}
