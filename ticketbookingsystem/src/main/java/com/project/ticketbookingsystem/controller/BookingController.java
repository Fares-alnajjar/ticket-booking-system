package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking/cart/add")
    public String addToCart(@RequestParam("eventId") Long eventId,
                            @RequestParam("ticketType") String ticketType,
                            @RequestParam("quantity") Integer quantity,
                            @RequestParam(value = "userId", required = false) Long userId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            bookingService.resolveCurrentUser(session, userId);
            bookingService.addToCart(session, eventId, ticketType, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket(s) added to cart successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/events/booking/" + eventId;
    }
}
