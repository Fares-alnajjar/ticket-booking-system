package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhoneNumber(Long phoneNumber);
}
