package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String showEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events";
    }
}
