package com.example.usermanagement.repository;

import com.example.usermanagement.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);
    
    @Modifying
    @Query("DELETE FROM Auth a WHERE a.user.id = :userId")
    void deleteByUserId(Long userId);
} 