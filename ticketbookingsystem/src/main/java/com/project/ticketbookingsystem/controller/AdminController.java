package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;

    public AdminController(EventService eventService) {
        this.eventService = eventService;
    }

    // ── GET /admin ────────────────────────────────────────────────────────────
    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("events",      eventService.getAllEvents());
        model.addAttribute("totalEvents", eventService.getTotalEvents());
        return "Admin/Admin";
    }

    // ── GET /admin/add-event ─────────────────────────────────────────────────
    @GetMapping("/add-event")
    public String getAddEventPage(Model model) {
        return "Admin/add event";
    }

    // ── POST /admin/add-event ─────────────────────────────────────────────────
    @PostMapping("/add-event")
    public String addEvent(
            @RequestParam(value = "eventName",   required = false) String eventName,
            @RequestParam(value = "category",    required = false) String category,
            @RequestParam(value = "eventDate",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
            @RequestParam(value = "eventTime",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime eventTime,
            @RequestParam(value = "location",    required = false) String location,
            @RequestParam(value = "ticketPrice", required = false) Double ticketPrice,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {

        if (eventName == null || eventName.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Event name is required.");
            return "redirect:/admin/add-event";
        }
        if (category == null || category.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Please select a category.");
            return "redirect:/admin/add-event";
        }
        if (eventDate == null) {
            redirectAttributes.addFlashAttribute("error", "Event date is required.");
            return "redirect:/admin/add-event";
        }
        if (eventTime == null) {
            redirectAttributes.addFlashAttribute("error", "Event time is required.");
            return "redirect:/admin/add-event";
        }
        if (location == null || location.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Location is required.");
            return "redirect:/admin/add-event";
        }
        if (ticketPrice == null || ticketPrice <= 0) {
            redirectAttributes.addFlashAttribute("error", "Ticket price must be greater than 0.");
            return "redirect:/admin/add-event";
        }
        if (description == null || description.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Description is required.");
            return "redirect:/admin/add-event";
        }

        EventEntity event = new EventEntity();
        event.setEventName(eventName);
        event.setCategory(EventEntity.Category.valueOf(category.toUpperCase()));
        event.setEventDate(eventDate);
        event.setEventTime(eventTime);
        event.setLocation(location);
        event.setTicketPrice(ticketPrice);
        event.setDescription(description);

        eventService.addEvent(event);

        redirectAttributes.addFlashAttribute("success", "Event \"" + eventName + "\" added successfully!");
        return "redirect:/admin";
    }

    // ── GET /admin/manage-events ──────────────────────────────────────────────
    // Shows the full event list with edit & delete buttons
    @GetMapping("/manage-events")
    public String getManageEventsPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "/admin/manage-events";
    }

    // ── GET /admin/edit-event/{id} ────────────────────────────────────────────
    // Loads the edit form pre-filled with the event's current data
    @GetMapping("/edit-event/{id}")
    public String getEditEventPage(@PathVariable Long id, Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            EventEntity event = eventService.getEventById(id);
            model.addAttribute("event", event);
            return "edit-event";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Event not found.");
            return "redirect:/admin/manage-events";
        }
    }

    // ── POST /admin/update-event ──────────────────────────────────────────────
    // Handles the edit form submission
    @PostMapping("/update-event")
    public String updateEvent(
            @RequestParam(value = "id")          Long id,
            @RequestParam(value = "eventName",   required = false) String eventName,
            @RequestParam(value = "category",    required = false) String category,
            @RequestParam(value = "eventDate",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
            @RequestParam(value = "eventTime",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime eventTime,
            @RequestParam(value = "location",    required = false) String location,
            @RequestParam(value = "ticketPrice", required = false) Double ticketPrice,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {

        if (eventName == null || eventName.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Event name is required.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (category == null || category.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Please select a category.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (eventDate == null) {
            redirectAttributes.addFlashAttribute("error", "Event date is required.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (eventTime == null) {
            redirectAttributes.addFlashAttribute("error", "Event time is required.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (location == null || location.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Location is required.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (ticketPrice == null || ticketPrice <= 0) {
            redirectAttributes.addFlashAttribute("error", "Ticket price must be greater than 0.");
            return "redirect:/admin/edit-event/" + id;
        }
        if (description == null || description.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Description is required.");
            return "redirect:/admin/edit-event/" + id;
        }

        EventEntity event = eventService.getEventById(id);
        event.setEventName(eventName);
        event.setCategory(EventEntity.Category.valueOf(category.toUpperCase()));
        event.setEventDate(eventDate);
        event.setEventTime(eventTime);
        event.setLocation(location);
        event.setTicketPrice(ticketPrice);
        event.setDescription(description);

        eventService.updateEvent(event);

        redirectAttributes.addFlashAttribute("success", "Event \"" + eventName + "\" updated successfully!");
        return "redirect:/Admin/manage-events";
    }

    // ── POST /admin/delete-event/{id} ─────────────────────────────────────────
    // Deletes an event by ID
    @PostMapping("/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            EventEntity event = eventService.getEventById(id);
            String name = event.getEventName();
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("success", "Event \"" + name + "\" deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Event not found.");
        }
        return "redirect:/admin/manage-events";
    }
}
