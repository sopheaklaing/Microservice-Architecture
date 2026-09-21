package com.sopheak.microservices.auth_service.client;

import com.sopheak.microservices.auth_service.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;
    private final String userServiceUrl;

    public UserClient(
            RestClient restClient,
            @Value("${user-service.url}") String userServiceUrl
    ) {
        this.restClient = restClient;
        this.userServiceUrl = userServiceUrl;
    }

    public void createUser(CreateUserRequest request) {

        restClient.post()
                .uri(userServiceUrl + "/api/users")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}