package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> findByUserIdOrderByBookingTimeDesc(Long userId);
}
