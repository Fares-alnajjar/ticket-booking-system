package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByEmail(String email);
}
