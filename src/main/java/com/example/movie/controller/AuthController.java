package com.example.movie.controller;

import com.example.movie.model.User;
import com.example.movie.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword) {

        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedEmail = email == null ? "" : email.trim();

        if (normalizedUsername.isBlank() || normalizedEmail.isBlank() || password == null || password.isBlank()) {
            return "redirect:/register?error=missing";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=passwordMismatch";
        }

        if (userRepository.findByUsername(normalizedUsername) != null) {
            return "redirect:/register?error=usernameExists";
        }

        if (userRepository.findByEmailIgnoreCase(normalizedEmail) != null) {
            return "redirect:/register?error=emailExists";
        }

        User newUser = new User(normalizedUsername, normalizedEmail, passwordEncoder.encode(password), "USER");
        userRepository.save(newUser);

        return "redirect:/login?registered";
    }
}
