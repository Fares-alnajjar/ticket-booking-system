package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity,Long> {
    Optional<TicketEntity> findByEventIdAndTypeIgnoreCase(Long eventId, String type);
}
