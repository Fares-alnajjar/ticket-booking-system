package com.project.ticketbookingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users") // table name in data base
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
}
