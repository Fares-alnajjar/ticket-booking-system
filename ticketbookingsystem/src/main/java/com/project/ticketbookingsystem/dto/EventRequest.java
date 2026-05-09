package com.project.ticketbookingsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {


    private Long id;

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotBlank(message = "Please select a category")
    private String category;

    @NotNull(message = "Event date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Event date cant be in the past")
    private LocalDate eventDate;

    @NotNull(message = "Event time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime eventTime;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "VIP price is required")
    @Positive(message = "VIP price must be greater than 0")
    private Double vipPrice;

    @NotNull(message = "Premium price is required")
    @Positive(message = "Premium price must be greater than 0")
    private Double premiumPrice;

    @NotNull(message = "Standard price is required")
    @Positive(message = "Standard price must be greater than 0")
    private Double standardPrice;

    @NotNull(message = "VIP capacity is required")
    @Min(value = 1, message = "VIP capacity must be at least 1")
    private Integer vipCapacity;

    @NotNull(message = "Premium capacity is required")
    @Min(value = 1, message = "Premium capacity must be at least 1")
    private Integer premiumCapacity;

    @NotNull(message = "Standard capacity is required")
    @Min(value = 1, message = "Standard capacity must be at least 1")
    private Integer standardCapacity;

    @NotNull(message = "ticket per user is required")
    @Min(value = 1, message = "ticket per user must be at least 1")
    private Integer ticketsPerUser;


    private String imageUrl;
}
