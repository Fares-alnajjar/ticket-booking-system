package com.project.ticketbookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sign_in")
public class SignIn {
    @GetMapping()
    public String getSignInPage() {
        return "sign_in";
    }

    @PostMapping()
    public String signIn(@RequestParam("category") String category,
                         @RequestParam("Email") String email,
                         @RequestParam("Password") String password,
                         RedirectAttributes redirectAttributes) {
        
        if (category == null || category.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select an authorization type");
            return "redirect:/sign_in";
        }

        if (email == null || email.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please enter your email");
            return "redirect:/sign_in";
        }

        if (password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please enter your password");
            return "redirect:/sign_in";
        }

        if ("admin".equalsIgnoreCase(category)) {
            return "redirect:/admin";
        } else if ("user".equalsIgnoreCase(category)) {
            return "redirect:/events";
        }

        redirectAttributes.addFlashAttribute("error", "Invalid authorization type");
        return "redirect:/sign_in";
    }
}
