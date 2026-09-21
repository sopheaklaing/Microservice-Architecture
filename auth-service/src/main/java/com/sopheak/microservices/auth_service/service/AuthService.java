package com.sopheak.microservices.auth_service.service;

import com.sopheak.microservices.auth_service.client.UserClient;
import com.sopheak.microservices.auth_service.dto.LoginRequest;
import com.sopheak.microservices.auth_service.dto.LoginResponse;
import com.sopheak.microservices.auth_service.dto.RegisterRequest;
import com.sopheak.microservices.auth_service.dto.RegisterResponse;
import com.sopheak.microservices.auth_service.dto.CreateUserRequest;
import com.sopheak.microservices.auth_service.entity.AuthUser;
import com.sopheak.microservices.auth_service.repository.AuthUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserClient userClient;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserClient userClient
    ) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String passwordHash =
                passwordEncoder.encode(request.getPassword());

        AuthUser authUser = new AuthUser(
                request.getEmail(),
                passwordHash,
                "USER"
        );

        AuthUser savedUser = authUserRepository.save(authUser);

        // Create user profile in User Service
        CreateUserRequest createUserRequest =
                new CreateUserRequest(
                        savedUser.getId(),
                        request.getName(),
                        savedUser.getEmail(),
                        savedUser.getRole()
                );

        userClient.createUser(createUserRequest);

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