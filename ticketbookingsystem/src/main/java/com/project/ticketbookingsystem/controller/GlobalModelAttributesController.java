package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributesController {
    private final BookingService bookingService;

    public GlobalModelAttributesController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ModelAttribute("cartItems")
    public Object cartItems(HttpSession session) {
        return bookingService.getCartItems(session);
    }

    @ModelAttribute("cartTotal")
    public double cartTotal(HttpSession session) {
        return bookingService.getCartTotal(session);
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        return bookingService.getCartCount(session);
    }
}
