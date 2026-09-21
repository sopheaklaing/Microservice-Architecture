package com.sopheak.microservices.auth_service.dto;

import java.util.UUID;

public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private UUID id;
    private String email;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String accessToken,
            String tokenType,
            UUID id,
            String email,
            String role
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}