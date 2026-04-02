package com.example.movie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private UserDetailServiceImpl userDetailsService;

    public SecurityConfig(UserDetailServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // the original, in-memory authentication
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/api/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .formLogin(formlogin -> formlogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/movies", true)
                        .permitAll())
                .logout(logout -> logout
                        .permitAll());
        return http.build();
    }

    // then apply permanent sign in
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
