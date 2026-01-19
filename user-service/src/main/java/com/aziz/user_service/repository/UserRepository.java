package com.aziz.user_service.repository;

import com.aziz.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    Optional<String> findEmailById(Long id);
}