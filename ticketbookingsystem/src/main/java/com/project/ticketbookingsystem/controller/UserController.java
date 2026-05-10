package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.dto.SignUpDto;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.repository.UserRepository;
import com.project.ticketbookingsystem.service.SignUpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            @Valid @ModelAttribute("updateProfileDto") SignUpDto dto,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/sign_in";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.SignUpDto", bindingResult);
            redirectAttributes.addFlashAttribute("updateProfileDto", dto);
            return "redirect:/user/profile";
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword((dto.getPassword()));


        signUpService.updateUser(user);
        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/user/profile";
    }
}
