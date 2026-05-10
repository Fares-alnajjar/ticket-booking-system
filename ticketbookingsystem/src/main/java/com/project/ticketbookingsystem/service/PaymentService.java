package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.model.PaymentEntity;
import com.project.ticketbookingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void validatePaymentData(String cardHolderName, String cardNumber, String expiryDate, String cvv) {
        if (cardHolderName == null || cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Card holder name is required.");
        }
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number is required.");
        }
        if (!isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Please enter a valid card number.");
        }
        if (!isValidExpiryDate(expiryDate)) {
            throw new IllegalArgumentException("Expiry date must be valid and not in the past.");
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("CVV must be 3 or 4 digits.");
        }
    }

    public void saveSuccessfulPayments(List<BookingEntity> bookings,
                                       String cardHolderName,
                                       String cardNumber,
                                       String expiryDate,
                                       String cvv) {
        String digitsOnlyCard = cardNumber.replaceAll("\\s+", "");
        for (BookingEntity booking : bookings) {
            PaymentEntity payment = PaymentEntity.builder()
                    .booking(booking)
                    .amount(booking.getTicket().getPrice())
                    .method("CARD")
                    .cardHolderName(cardHolderName.trim())
                    .cardNumber(digitsOnlyCard)
                    .expiryDate(expiryDate)
                    .cvv(cvv)
                    .status("SUCCESS")
                    .paymentTime(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        String digitsOnly = cardNumber.replaceAll("\\s+", "");
        if (!digitsOnly.matches("\\d{13,19}")) {
            return false;
        }

        int sum = 0;
        boolean shouldDouble = false;
        for (int i = digitsOnly.length() - 1; i >= 0; i--) {
            int digit = digitsOnly.charAt(i) - '0';
            if (shouldDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            shouldDouble = !shouldDouble;
        }
        return sum % 10 == 0;
    }

    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }

        try {
            YearMonth entered = YearMonth.parse(expiryDate, DateTimeFormatter.ofPattern("MM/yy"));
            return !entered.isBefore(YearMonth.now());
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
