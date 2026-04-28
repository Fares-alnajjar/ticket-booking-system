package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Tickets")
@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //note : this is Long not long (Wrapper class)
    @Column(nullable = false)
    private String type; //(vip/reqular) will be determined
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Integer availableSeats; //Integear -> wrapper class, in ticket to differentiate between vip/regular/..
    // additional column for event object(FK)
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

}
