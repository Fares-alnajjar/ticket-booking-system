package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.service.BookingService;
import com.project.ticketbookingsystem.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    public EventController(EventService eventService, BookingService bookingService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String showEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events";
    }

    @GetMapping("/booking/{id}")
    public String showBooking(@PathVariable Long id,
                              @RequestParam(value = "userId", required = false) Long userId,
                              HttpSession session,
                              Model model) {
        EventEntity event = eventService.getEventById(id);
        model.addAttribute("event", event);
        try {
            model.addAttribute("currentUserId", bookingService.resolveCurrentUser(session, userId).getId());
        } catch (IllegalArgumentException ignored) {
            model.addAttribute("currentUserId", null);
        }
        return "Booking";
    }
}
