package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository  extends JpaRepository<EventEntity,Long> {

    List<EventEntity> findByCategory(EventEntity.Category category);
}
