package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "event") // "event"variable name in owner side
    List<TicketEntity> tickets; // need to know why List not arrayList (me too) hahaha

}
