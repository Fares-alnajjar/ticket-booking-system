package com.project.ticketbookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign_in")
public class SignIn {
    @GetMapping()
    public String getSignInPage() {
        return "sign_in";
    }
}
