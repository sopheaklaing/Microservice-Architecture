package com.sopheak.microservices.user_service.service;

import com.sopheak.microservices.user_service.dto.UserRequest;
import com.sopheak.microservices.user_service.dto.UserResponse;
import com.sopheak.microservices.user_service.entity.User;
import com.sopheak.microservices.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.sopheak.microservices.user_service.exception.UserNotFoundException;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {
        // create object fron entity
        User user = new User(
                request.getName(),
                request.getEmail()
        );
        // save data to postgres Db 
        User savedUser = userRepository.save(user);
        // when resopne result to UI
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
    // paht for get all User 
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }
    // this is paht for get User by ID 
    public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
            // this line cell own Exception
            .orElseThrow(() -> new UserNotFoundException(id));

    return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
       );
    }
    public UserResponse updateUser(Long id, UserRequest request){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);
        return new UserResponse(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail()
        );
    }

    public void deleteUser(Long id) {

    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

    userRepository.delete(user);
    
    }

}