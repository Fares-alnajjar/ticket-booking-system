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
//    @GetMapping("/sign_in")
//    public String getSignInPage() {
//        return "sign_in";
//    }
    @GetMapping("/sign_in")
    public String getSignInPage(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if (user != null) {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            }
            return "redirect:/events";
        }
        return "sign_in";
    }


    @PostMapping("/sign_in")
    public String handleSignIn(
            @RequestParam("Email") String email,
            @RequestParam("Password") String password,
            @RequestParam(value = "redirect", required = false) String redirectUrl,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session) {
        try {
            SignInDto request = new SignInDto();
            request.setEmail(email);
            request.setPassword(password);

            UserEntity user = signInService.handlingSignIn(request);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("CURRENT_USER_ID", user.getId());
//            redirectAttributes.addFlashAttribute("successMessage",
//                    "Welcome back, " + user.getName() + "! You have signed in successfully.");
//            return "redirect:/events";
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            }
            else if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            else {
                redirectAttributes.addFlashAttribute("successMessage",
                    "Welcome back, " + user.getName() + "! You have signed in successfully.");
                return "redirect:/events";
            }

        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "sign_in";
        }
    }
}
