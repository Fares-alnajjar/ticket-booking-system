package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.service.BookingService;
import com.project.ticketbookingsystem.service.EventScheduleService;
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
    private final EventScheduleService eventScheduleService;

    public EventController(EventService eventService, BookingService bookingService, EventScheduleService eventScheduleService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.eventScheduleService = eventScheduleService;
    }

    @GetMapping
    public String showEvents(@RequestParam(value = "category", required = false) String category,
                             Model model) {
        model.addAttribute("events", eventService.getEventsByCategorySelection(category));
        model.addAttribute("selectedCategory", eventService.normalizeSelectedCategoryLabel(category));
        return "events";
    }

    @GetMapping("/booking/{id}")
    public String showBooking(@PathVariable Long id,
                              @RequestParam(value = "userId", required = false) Long userId,
                              HttpSession session,
                              Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/sign_in?redirect=/events/booking/" + id;
        }
        
        EventEntity event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("bookingAllowed", eventScheduleService.isOpenForBooking(event));
        model.addAttribute("bookingClosedMessage", eventScheduleService.getBookingClosedMessage(event));
        try {
            model.addAttribute("currentUserId", bookingService.resolveCurrentUser(session, userId).getId());
        } catch (IllegalArgumentException ignored) {
            model.addAttribute("currentUserId", null);
        }
        return "Booking";
    }
}
