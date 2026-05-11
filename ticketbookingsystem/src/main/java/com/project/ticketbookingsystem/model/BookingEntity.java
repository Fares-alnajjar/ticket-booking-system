package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings") // table name in data base
@Data               // setters/getters
@NoArgsConstructor
@AllArgsConstructor
@Builder // to handle long constructors
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // unique values generated automatically for id(primary key)
    private Long id;
@Column(nullable = false)
   private LocalDateTime bookingTime=LocalDateTime.now();
@Column(nullable = false)
    private String status; // confirmed or cancelled
    // no quantity for now
    // relations between this table and other tables
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name="ticket_id",nullable = false)
    private TicketEntity ticket;
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentEntity payment;
}
