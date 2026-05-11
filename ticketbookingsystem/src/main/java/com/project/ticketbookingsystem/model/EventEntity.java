package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private String eventName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    public enum Category {
        FOOTBALL, BASKETBALL, HANDBALL,OTHERS
    }

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private LocalTime eventTime;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;


    @Column(nullable = false)
    private Double vipPrice;

    @Column(nullable = false)
    private Double premiumPrice;

    @Column(nullable = false)
    private Double standardPrice;


    @Column(nullable = false)
    private Integer vipCapacity;

    @Column(nullable = false)
    private Integer premiumCapacity;

    @Column(nullable = false)
    private Integer standardCapacity;


    @Column(length = 1000)
    private String imageUrl;

    @Column(nullable = false)
    private Integer ticketsPerUser;

    /**
     * How long the event runs from start (event date + time), in minutes.
     * If null (legacy rows), {@link EventScheduleService} treats this as 60 minutes.
     */
    private Integer durationMinutes;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true) // "event"variable name in owner side
    List<TicketEntity> tickets; // need to know why List not arrayList (me too) hahaha

}
