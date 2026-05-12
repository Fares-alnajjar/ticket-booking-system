package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.dto.SignUpDto;
import com.project.ticketbookingsystem.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class SignUpController {
    private final SignUpService signUpService ;
    @Autowired
    public SignUpController(SignUpService signUpService){
        this.signUpService=signUpService;
    }

    @GetMapping("/sign_up")
    public String getSignUpPage() { return "sign_up"; }

    @PostMapping("/sign_up")
    public String handleSignUp(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("nationalId") String nationalId,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("conPass") String conpass,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (!password.equals(conpass)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "sign_up";
        }
        try {
            SignUpDto request = new SignUpDto();
            request.setName(name);
            request.setPhoneNumber(phone);
            request.setNationalId(nationalId);
            request.setEmail(email);
            request.setPassword(password);

            signUpService.register(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Account created successfully! Please sign in");
            return "redirect:/sign_in";

        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "sign_up";
        }catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage()); // اما نشوف الحكايه دي
            return "sign_up";
        }
    }
}
