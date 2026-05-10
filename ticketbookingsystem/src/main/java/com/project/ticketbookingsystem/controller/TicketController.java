package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TicketController {
    private final BookingService bookingService;

    public TicketController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/my-tickets")
    public String showMyTickets(@RequestParam(value = "userId", required = false) Long userId,
                                HttpSession session,
                                Model model) {
        try {
            UserEntity user = bookingService.resolveCurrentUser(session, userId);
            List<BookingEntity> bookings = bookingService.getUserBookings(user.getId());
            model.addAttribute("bookings", bookings);
            model.addAttribute("currentUserId", user.getId());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("bookings", List.of());
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "my-tickets";
    }
}
