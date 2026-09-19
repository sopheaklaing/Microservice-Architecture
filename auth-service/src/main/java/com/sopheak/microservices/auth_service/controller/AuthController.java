package com.sopheak.microservices.auth_service.controller;

import com.sopheak.microservices.auth_service.dto.RegisterRequest;
import com.sopheak.microservices.auth_service.dto.RegisterResponse;
import com.sopheak.microservices.auth_service.dto.LoginRequest;
import com.sopheak.microservices.auth_service.dto.LoginResponse;
import com.sopheak.microservices.auth_service.dto.MeResponse;
import org.springframework.security.core.Authentication;

import com.sopheak.microservices.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // this is route register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // this is route for login
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {

        String userId = authentication.getName();
        String email = (String) authentication.getDetails();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("");

        return new MeResponse(
                Long.valueOf(userId),
                email,
                role.replace("ROLE_", ""));
    }
}
