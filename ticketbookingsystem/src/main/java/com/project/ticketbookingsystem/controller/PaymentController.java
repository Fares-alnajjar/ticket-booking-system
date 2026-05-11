package com.project.ticketbookingsystem.controller;

import com.project.ticketbookingsystem.dto.CartItemDto;
import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.model.EventEntity;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.service.BookingService;
import com.project.ticketbookingsystem.service.EventScheduleService;
import com.project.ticketbookingsystem.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PaymentController {
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final EventScheduleService eventScheduleService;

    public PaymentController(BookingService bookingService,
                             PaymentService paymentService,
                             EventScheduleService eventScheduleService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.eventScheduleService = eventScheduleService;
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam(value = "userId", required = false) Long userId,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = bookingService.resolveCurrentUser(session, userId);
            if (bookingService.getCartItems(session).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Add tickets first.");
                return "redirect:/events";
            }
            assertCartStillBookable(session);
            model.addAttribute("currentUserId", user.getId());
            model.addAttribute("cartItems", bookingService.getCartItems(session));
            model.addAttribute("cartTotal", bookingService.getCartTotal(session));
            return "payment";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/events";
        }
    }

    @PostMapping("/payment")
    public String payNow(@RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam("cardHolderName") String cardHolderName,
                         @RequestParam("cardNumber") String cardNumber,
                         @RequestParam("expiryDate") String expiryDate,
                         @RequestParam("cvv") String cvv,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = bookingService.resolveCurrentUser(session, userId);
            assertCartStillBookable(session);
            paymentService.validatePaymentData(cardHolderName, cardNumber, expiryDate, cvv);

            List<BookingEntity> createdBookings = bookingService.createBookingsFromCart(session, user);
            paymentService.saveSuccessfulPayments(createdBookings, cardHolderName, cardNumber, expiryDate, cvv);

            redirectAttributes.addFlashAttribute("successMessage", "Payment completed successfully.");
            return "redirect:/confirmation";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/payment";
        }
    }

    @GetMapping("/cart")
    public String showCartPage(HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            if (bookingService.getCartItems(session).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Add tickets first.");
                return "redirect:/events";
            }
            assertCartStillBookable(session);
            model.addAttribute("cartItems", bookingService.getCartItems(session));
            model.addAttribute("cartTotal", bookingService.getCartTotal(session));
            return "cart";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/events";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(HttpSession session, Model model) {
        return "confirmation";
    }

    private void assertCartStillBookable(HttpSession session) {
        for (CartItemDto item : bookingService.getCartItems(session)) {
            EventEntity event = bookingService.getEventById(item.getEventId());
            if (!eventScheduleService.isOpenForBooking(event)) {
                throw new IllegalArgumentException(
                        "Your cart includes \"" + event.getEventName() + "\" which can no longer be booked. "
                                + eventScheduleService.getBookingClosedMessage(event));
            }
        }
    }

}
