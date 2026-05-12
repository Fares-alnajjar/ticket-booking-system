package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.AdminDashboardRevenue;
import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.model.PaymentEntity;
import com.project.ticketbookingsystem.repository.PaymentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public PaymentService(PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
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
        String encodedCardNumber = passwordEncoder.encode(digitsOnlyCard);
        for (BookingEntity booking : bookings) {
            PaymentEntity payment = PaymentEntity.builder()
                    .booking(booking)
                    .amount(booking.getTicket().getPrice())
                    .method("CARD")
                    .cardHolderName(cardHolderName.trim())
                    .cardNumber(encodedCardNumber)
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

    /**
     * All dashboard revenue figures in two DB round-trips (no full-table loads, no N+1 through the graph).
     */
    public AdminDashboardRevenue getAdminDashboardRevenue() {
        Double total = paymentRepository.sumAllPaymentAmounts();
        double totalRev = total != null ? total : 0.0;

        Map<EventEntity.Category, Double> byCategory = new EnumMap<>(EventEntity.Category.class);
        for (EventEntity.Category c : EventEntity.Category.values()) {
            byCategory.put(c, 0.0);
        }
        for (Object[] row : paymentRepository.sumAmountGroupedByEventCategory()) {
            EventEntity.Category category = (EventEntity.Category) row[0];
            Number amount = (Number) row[1];
            byCategory.put(category, amount != null ? amount.doubleValue() : 0.0);
        }

        return new AdminDashboardRevenue(
                totalRev,
                byCategory.getOrDefault(EventEntity.Category.FOOTBALL, 0.0),
                byCategory.getOrDefault(EventEntity.Category.BASKETBALL, 0.0),
                byCategory.getOrDefault(EventEntity.Category.HANDBALL, 0.0),
                byCategory.getOrDefault(EventEntity.Category.OTHERS, 0.0));
    }

    public double getTotalRevenue() {
        Double v = paymentRepository.sumAllPaymentAmounts();
        return v != null ? v : 0.0;
    }
}
