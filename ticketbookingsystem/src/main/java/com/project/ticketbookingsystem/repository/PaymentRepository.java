package com.project.ticketbookingsystem.repository;

import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    /** Total of all payment rows (matches legacy {@code findAll()} + sum behavior). */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentEntity p")
    Double sumAllPaymentAmounts();

    /**
     * Revenue grouped by related event category (one aggregated read instead of loading all payments).
     */
    @Query("SELECT e.category, COALESCE(SUM(p.amount), 0) FROM PaymentEntity p "
            + "JOIN p.booking b JOIN b.ticket t JOIN t.event e GROUP BY e.category")
    List<Object[]> sumAmountGroupedByEventCategory();
}
