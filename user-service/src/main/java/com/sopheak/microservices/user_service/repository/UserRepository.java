package com.sopheak.microservices.user_service.repository;
import com.sopheak.microservices.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

}
