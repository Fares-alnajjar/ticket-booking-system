package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Events")
@Entity
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private LocalDateTime date; // time in format like this -> 2026-04-29 18:30
    @OneToMany(mappedBy = "event") // "event"variable name in owner side
    List<TicketEntity> tickets; // need to know why List not arrayList (me too) hahaha

}
