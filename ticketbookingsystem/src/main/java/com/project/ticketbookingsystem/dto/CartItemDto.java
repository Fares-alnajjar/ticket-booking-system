package com.project.ticketbookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long eventId;
    private String eventName;
    private String ticketType;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
