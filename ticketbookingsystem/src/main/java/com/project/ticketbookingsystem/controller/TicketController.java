package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;



@Controller
public class TicketController {

    @GetMapping("/my-tickets")
    public String showMyTickets() {
        return "my-tickets";
    }
}



