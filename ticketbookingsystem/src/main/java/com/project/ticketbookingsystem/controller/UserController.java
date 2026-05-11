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
            @Valid @ModelAttribute("user") SignUpDto request,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/sign_in";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.user",
                    bindingResult);
            redirectAttributes.addFlashAttribute("user", request);
            return "redirect:/user/profile";
        }

        try {
            // set the current user's id so service knows which user to update
            request.setId(user.getId());
            signUpService.updateUser(request);

            // refresh session with updated user
            UserEntity updatedUser = signUpService.getUserById(user.getId());
            session.setAttribute("loggedInUser", updatedUser);

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("user", request);
        }
        return "redirect:/user/profile";
    }
}
