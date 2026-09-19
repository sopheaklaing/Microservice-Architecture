package com.sopheak.microservices.auth_service.service;

import com.sopheak.microservices.auth_service.dto.RegisterRequest;
import com.sopheak.microservices.auth_service.dto.RegisterResponse;
import com.sopheak.microservices.auth_service.entity.AuthUser;
import com.sopheak.microservices.auth_service.repository.AuthUserRepository;
import com.sopheak.microservices.auth_service.dto.LoginRequest;
import com.sopheak.microservices.auth_service.dto.LoginResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
            
    ) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        
        // this exception we make 
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        AuthUser authUser = new AuthUser(
                request.getEmail(),
                passwordHash,
                "USER"
        );

        AuthUser savedUser = authUserRepository.save(authUser);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
    public LoginResponse login(LoginRequest request) {

        AuthUser authUser = authUserRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        authUser.getPasswordHash()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(
                authUser.getId(),
                authUser.getEmail(),
                authUser.getRole()
        );

        return new LoginResponse(
                accessToken,
                "Bearer",
                authUser.getId(),
                authUser.getEmail(),
                authUser.getRole()
        );
    }
}
