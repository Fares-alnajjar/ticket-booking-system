package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.service.SignUpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
    private final SignUpService signUpService;

    @Autowired
    public UserController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/sign_in";
        }
        model.addAttribute("user", user);
        return "user-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "password", required = false) String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/sign_in";
        }

        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        signUpService.updateUser(user);
        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/user/profile";
    }
}
