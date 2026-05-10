package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.model.UserEntity;
import org.springframework.ui.Model;
import com.project.ticketbookingsystem.dto.SignInDto;
import com.project.ticketbookingsystem.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class SignInController {
    private final SignInService signInService;

    @Autowired
    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }
    @GetMapping("/sign_in")
    public String getSignInPage() {
        return "sign_in";
    }

    @PostMapping("/sign_in")
    public String handleSignIn(
            @RequestParam("Email") String email,
            @RequestParam("Password") String password,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session) {
        try {
            SignInDto request = new SignInDto();
            request.setEmail(email);
            request.setPassword(password);

            UserEntity user = signInService.handlingSignIn(request);
            session.setAttribute("loggedInUser", user);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Welcome back, " + user.getName() + "! You have signed in successfully.");
            return "redirect:/events";

        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "sign_in";
        }
    }
}

//
//
//@Controller
//@RequestMapping("/sign_in")
//public class SignInController {
//    @GetMapping()
//    public String getSignInPage() {
//        return "sign_in";
//    }
//
//    @PostMapping()
//    public String signIn(@RequestParam(value = "category", required = false) String category,
//                         @RequestParam(value = "Email", required = false) String email,
//                         @RequestParam(value = "Password", required = false) String password,
//                         RedirectAttributes redirectAttributes) {
//
//        if (category == null || category.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Please select an authorization type");
//            return "redirect:/sign_in";
//        }
//
//        if (email == null || email.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Please enter your email");
//            return "redirect:/sign_in";
//        }
//
//        if (password == null || password.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Please enter your password");
//            return "redirect:/sign_in";
//        }
//
//        if ("admin".equalsIgnoreCase(category)) {
//            return "redirect:/admin";
//        } else if ("user".equalsIgnoreCase(category)) {
//            return "redirect:/events";
//        }
//
//
//        return "redirect:/sign_in";
//    }
//}