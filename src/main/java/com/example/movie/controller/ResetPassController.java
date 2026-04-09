package com.example.movie.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.movie.model.User;
import com.example.movie.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class ResetPassController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
            @RequestParam String password) {

        User user = userRepository.findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return "redirect:/login?invalidToken";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }
}
