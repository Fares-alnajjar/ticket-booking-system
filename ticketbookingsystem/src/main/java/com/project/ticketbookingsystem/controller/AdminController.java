package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.dto.EventRequest;
import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.service.EventService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;

    public AdminController(EventService eventService) {
        this.eventService = eventService;
    }

    private void mapRequestToEntity(EventRequest request, EventEntity event) {
        event.setEventName(request.getEventName());
        event.setCategory(EventEntity.Category.valueOf(request.getCategory().toUpperCase()));
        event.setEventDate(request.getEventDate());
        event.setEventTime(request.getEventTime());
        event.setLocation(request.getLocation());
        event.setDescription(request.getDescription());
        event.setVipPrice(request.getVipPrice());
        event.setPremiumPrice(request.getPremiumPrice());
        event.setStandardPrice(request.getStandardPrice());
        event.setVipCapacity(request.getVipCapacity());
        event.setPremiumCapacity(request.getPremiumCapacity());
        event.setStandardCapacity(request.getStandardCapacity());
        event.setImageUrl(request.getImageUrl());
        event.setTicketsPerUser(request.getTicketsPerUser());
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
//            @RequestParam(value = "ticketPrice", required = false) Double ticketPrice,
            @RequestParam(value = "description", required = false) String description,
    public String addEvent(@ModelAttribute @Valid EventRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/admin/add-event";
        }

        EventEntity event = new EventEntity();
        mapRequestToEntity(request, event);
        eventService.addEvent(event);

        redirectAttributes.addFlashAttribute("success",
                "Event \"" + request.getEventName() + "\" added successfully!");
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
            @Valid EventRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/admin/edit-event/" + request.getId();
        }

        try {
            EventEntity event = eventService.getEventById(request.getId());
            mapRequestToEntity(request, event);
            eventService.updateEvent(event);

            redirectAttributes.addFlashAttribute("success",
                    "Event \"" + request.getEventName() + "\" updated successfully!");
            return "redirect:/admin/manage-events";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Event not found.");
            return "redirect:/admin/manage-events";
        }
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
