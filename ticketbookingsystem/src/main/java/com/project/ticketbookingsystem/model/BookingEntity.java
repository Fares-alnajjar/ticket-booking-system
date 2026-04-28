package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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




}
