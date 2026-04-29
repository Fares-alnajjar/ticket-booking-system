package com.project.ticketbookingsystem.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double amount; // one ticket (need tazbet)
// هنثبت الشراء بالكارت فقط
    @Column(nullable = false)
    private String method = "CARD";
    // بيانات الكارت
    @Column(nullable = false)
    private String cardHolderName;
    @Column(nullable = false)
    private String cardNumber; // need to be hashed like password in user
    @Column(nullable = false)
    private String expiryDate;
    @Column(nullable = false)
    private String cvv; //   بتبقي ب gateway or token/ بيقولك مش بيتخزن بس مشيها كده دلوقتي

    @Column(nullable = false)
    private String status; // SUCCESS / FAILED
    @Column(nullable = false)
    private LocalDateTime paymentTime;
    // ال payment هيبقي ال owner side
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private BookingEntity booking;

}
