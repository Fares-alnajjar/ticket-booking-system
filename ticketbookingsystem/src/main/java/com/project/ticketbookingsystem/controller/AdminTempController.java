package com.project.ticketbookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminTempController {
    @GetMapping("/admin")
    public String admin() {
        return "Admin/Admin";
    }

    @GetMapping("/admin/add-event")
    public String addEvent() {
        return "Admin/add event";
    }

    @GetMapping("/admin/manage-events")
    public String manageEvents() { return "Admin/manage-events"; }

    @GetMapping("/admin/manage-users")
    public String manageUsers() { return "Admin/manage users"; }
}
