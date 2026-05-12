package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.dto.EventRequest;
import com.project.ticketbookingsystem.dto.SignUpDto;
import com.project.ticketbookingsystem.dto.UpdateProfileDto;
import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.model.TicketEntity;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.service.BookingService;
import com.project.ticketbookingsystem.service.EventService;
import com.project.ticketbookingsystem.service.PaymentService;
import com.project.ticketbookingsystem.service.SignUpService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final SignUpService signUpService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public AdminController(EventService eventService,PaymentService paymentService, SignUpService signUpService, BookingService bookingService) {
        this.eventService = eventService;
        this.signUpService = signUpService;
        this.bookingService = bookingService;
        this.paymentService=paymentService;
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
        event.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 60);
    }

    // ── GET /admin ────────────────────────────────────────────────────────────
    @GetMapping
    public String getAdminPage(Model model) {

        model.addAttribute("totalEvents", eventService.getTotalEvents());

        model.addAttribute("totalUsers",  signUpService.getTotalUsers());

        model.addAttribute("totalTickets",bookingService.getTotalTicketCount());

        model.addAttribute("totalRevenue",paymentService.getTotalRevenue());

        model.addAttribute("footballRevenue",paymentService.getFootballRevenue());

        model.addAttribute("basketballRevenue",paymentService.getBasketballRevenue());

        model.addAttribute("handballRevenue",paymentService.getHandballRevenue());

        model.addAttribute("othersRevenue",paymentService.getOthersRevenue());
        return "Admin/Admin";
    }

    // ── GET /admin/add-event ─────────────────────────────────────────────────
    @GetMapping("/add-event")
    public String getAddEventPage(Model model) {
        return "Admin/add event";
    }

    // ── POST /admin/add-event ─────────────────────────────────────────────────
    @PostMapping("/add-event")
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
        return "redirect:/admin/add-event";
    }

    // ── GET /admin/manage-events ──────────────────────────────────────────────
    // Shows the full event list with edit & delete buttons
    @GetMapping("/manage-events")
    public String getManageEventsPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "Admin/manage-events";
    }

    // ── GET /admin/manage-users ───────────────────────────────────────────────
    // Shows the full user list
    @GetMapping("/manage-users")
    public String getManageUsersPage(Model model) {
        model.addAttribute("users", signUpService.getAllUsers());
        return "Admin/manage-users";
    }

    // ── GET /admin/edit-event/{id} ────────────────────────────────────────────
    // Loads the edit form pre-filled with the event's current data
    @GetMapping("/edit-event/{id}")
    public String getEditEventPage(@PathVariable Long id, Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            EventEntity event = eventService.getEventById(id);

            EventRequest request = new EventRequest();
            request.setId(event.getId());
            request.setEventName(event.getEventName());
            request.setCategory(event.getCategory().name());
            request.setEventDate(event.getEventDate());
            request.setEventTime(event.getEventTime());
            request.setLocation(event.getLocation());
            request.setDescription(event.getDescription());
            request.setVipPrice(event.getVipPrice());
            request.setPremiumPrice(event.getPremiumPrice());
            request.setStandardPrice(event.getStandardPrice());
            request.setVipCapacity(event.getVipCapacity());
            request.setPremiumCapacity(event.getPremiumCapacity());
            request.setStandardCapacity(event.getStandardCapacity());
            request.setImageUrl(event.getImageUrl());
            request.setTicketsPerUser(event.getTicketsPerUser());
            request.setDurationMinutes(event.getDurationMinutes() != null ? event.getDurationMinutes() : 60);

            model.addAttribute("event", request);
            return "Admin/edit-event";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Event not found.");
            return "redirect:/admin/manage-events";
        }
    }

    // ── POST /admin/update-event ──────────────────────────────────────────────
    // Handles the edit form submission
    @PostMapping("/update-event")

    public String updateEvent(
            @ModelAttribute @Valid EventRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().get(0).getDefaultMessage();
            model.addAttribute("error", errorMsg);
            model.addAttribute("event", request);
            return "Admin/edit-event";
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
    //DELETE USER
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = signUpService.getUserById(id);
            String name = user.getName();
            signUpService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User \"" + name + "\" deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/manage-users";
    }
    /*@GetMapping("/edit-user/{id}")
    public String getEditUserPage(@PathVariable Long id, Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = signUpService.getUserById(id);

            SignUpDto request = new SignUpDto();
            request.setId(user.getId());
            request.setName(user.getName());
            request.setEmail(user.getEmail());

            request.setNationalId(user.getNationalId());
            request.setPhoneNumber(user.getPhoneNumber());


            model.addAttribute("user", request);
            return "Admin/edit-user";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/manage-users";
        }
    }*/
    @GetMapping("/edit-user/{id}")
    public String getEditUserPage(@PathVariable Long id, Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = signUpService.getUserById(id);

            UpdateProfileDto request = UpdateProfileDto.builder() // correct DTO
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .nationalId(user.getNationalId())
                    .phoneNumber(user.getPhoneNumber())
                    .createdAt(user.getCreatedAt())
                    .build();

            model.addAttribute("user", request);
            return "Admin/edit-user";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/manage-users";
        }
    }

   /* @PostMapping("/update-user")
    public String updateUser(
            @ModelAttribute @Valid UpdateProfileDto request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().get(0).getDefaultMessage();
            model.addAttribute("error", errorMsg);
            model.addAttribute("event", request);
            return "Admin/edit-user";
        }
        try {
            signUpService.updateUser(request);

            redirectAttributes.addFlashAttribute("success",
                    "Event \"" + request.getName() + "\" updated successfully!");
            return "redirect:/admin/manage-users";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Event not found.");
            return "redirect:/admin/manage-users";
        }


    }*/
   @PostMapping("/update-user")
   public String updateUser(
           @ModelAttribute @Valid UpdateProfileDto request,
           BindingResult result,
           Model model,
           RedirectAttributes redirectAttributes) {

       if (result.hasErrors()) {
           String errorMsg = result.getFieldErrors().get(0).getDefaultMessage();
           model.addAttribute("error", errorMsg);
           model.addAttribute("user", request); //  was "event", should be "user"
           return "Admin/edit-user";
       }
       try {
           signUpService.updateUser(request);
           redirectAttributes.addFlashAttribute("success",
                   "User \"" + request.getName() + "\" updated successfully!"); //  was "Event"
           return "redirect:/admin/manage-users";

       } catch (IllegalArgumentException e) {
           model.addAttribute("error", e.getMessage()); //  show actual error message
           model.addAttribute("user", request);
           return "Admin/edit-user"; //  return view directly not redirect (keeps errors)
       }
   }

}
