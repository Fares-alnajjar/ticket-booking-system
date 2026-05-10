package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users") // table name in database
@Data               // setters/getters
@NoArgsConstructor
@AllArgsConstructor
@Builder // to handle long constructors
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // unique values generated automatically for id(primary key)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false,unique = true)
    private Long nationalId;
    @Column(nullable = false,unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String role; // admin or user
    @Column(name = "created_at", updatable = false)  //
    private LocalDateTime createdAt; //

    @OneToMany(mappedBy = "user")
    private List<BookingEntity> bookings;
    
    @PrePersist  //
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
