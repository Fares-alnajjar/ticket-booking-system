package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.CartItemDto;
import com.project.ticketbookingsystem.model.*;
import com.project.ticketbookingsystem.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BookingService {
    private static final String CART_SESSION_KEY = "CART_ITEMS";
    private static final String CURRENT_USER_ID_SESSION_KEY = "CURRENT_USER_ID";
    private static final String LOGGED_IN_USER_SESSION_KEY = "loggedInUser";

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final EventScheduleService eventScheduleService;

    public BookingService(EventRepository eventRepository,
                          TicketRepository ticketRepository,
                          BookingRepository bookingRepository,
                          UserRepository userRepository,
                          PaymentRepository paymentRepository,
                          EventScheduleService eventScheduleService) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.paymentRepository=paymentRepository;
        this.eventScheduleService = eventScheduleService;
    }

    public EventEntity getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));
    }

    public void addToCart(HttpSession session, Long eventId, String ticketType, Integer quantity) {
        EventEntity event = getEventById(eventId);
        requireBookingAllowed(event);
        int requestedQuantity = validateQuantity(event, quantity);
        String normalizedType = normalizeTicketType(ticketType);
        int availableSeats = getAvailableSeats(event, normalizedType);

        if (requestedQuantity > availableSeats) {
            throw new IllegalArgumentException("Only " + availableSeats + " seats are available for " + normalizedType + " tickets.");
        }

        List<CartItemDto> cartItems = getCartItems(session);
        Optional<CartItemDto> existingItem = cartItems.stream()
                .filter(item -> item.getEventId().equals(eventId) && item.getTicketType().equalsIgnoreCase(normalizedType))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemDto item = existingItem.get();
            int newQuantity = item.getQuantity() + requestedQuantity;

            if (newQuantity > event.getTicketsPerUser()) {
                throw new IllegalArgumentException("Maximum tickets per user is " + event.getTicketsPerUser() + " for this event.");
            }
            if (newQuantity > availableSeats) {
                throw new IllegalArgumentException("Only " + availableSeats + " seats are available for " + normalizedType + " tickets.");
            }

            item.setQuantity(newQuantity);
            item.setTotalPrice(newQuantity * item.getUnitPrice());
        } else {
            double price = getPriceByType(event, normalizedType);
            cartItems.add(CartItemDto.builder()
                    .eventId(eventId)
                    .eventName(event.getEventName())
                    .ticketType(normalizedType)
                    .quantity(requestedQuantity)
                    .unitPrice(price)
                    .totalPrice(price * requestedQuantity)
                    .build());
        }

        session.setAttribute(CART_SESSION_KEY, cartItems);
    }

    public List<CartItemDto> getCartItems(HttpSession session) {
        Object cartObj = session.getAttribute(CART_SESSION_KEY);
        if (cartObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<CartItemDto> cartItems = (List<CartItemDto>) cartObj;
            return new ArrayList<>(cartItems);
        }
        return new ArrayList<>();
    }

    public double getCartTotal(HttpSession session) {
        return getCartItems(session).stream().mapToDouble(CartItemDto::getTotalPrice).sum();
    }

    public int getCartCount(HttpSession session) {
        return getCartItems(session).stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    public void clearCart(HttpSession session) {
        session.setAttribute(CART_SESSION_KEY, new ArrayList<CartItemDto>());
    }

    public UserEntity resolveCurrentUser(HttpSession session, Long userId) {
        Long activeUserId = null;

        Object loggedInUserObj = session.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        if (loggedInUserObj instanceof UserEntity loggedInUser) {
            activeUserId = loggedInUser.getId();
            if (userId != null && !userId.equals(activeUserId)) {
                throw new IllegalArgumentException("User id does not match the signed-in account.");
            }
        }

        if (activeUserId == null) {
            Object userSessionObject = session.getAttribute(CURRENT_USER_ID_SESSION_KEY);
            if (userSessionObject instanceof Long id) {
                activeUserId = id;
            }
        }

        if (activeUserId == null) {
            activeUserId = userId;
        }

        if (activeUserId == null) {
            throw new IllegalArgumentException("Please sign in first.");
        }

        Long resolvedUserId = activeUserId;
        UserEntity user = userRepository.findById(resolvedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + resolvedUserId));

        session.setAttribute(CURRENT_USER_ID_SESSION_KEY, user.getId());
        session.setAttribute(LOGGED_IN_USER_SESSION_KEY, user);
        return user;
    }

    public List<BookingEntity> createBookingsFromCart(HttpSession session, UserEntity user) {
        List<CartItemDto> cartItems = getCartItems(session);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }

        List<BookingEntity> savedBookings = new ArrayList<>();

        for (CartItemDto item : cartItems) {
            EventEntity event = getEventById(item.getEventId());
            requireBookingAllowed(event);
            String normalizedType = normalizeTicketType(item.getTicketType());
            int availableSeats = getAvailableSeats(event, normalizedType);

            if (item.getQuantity() > availableSeats) {
                throw new IllegalArgumentException("Not enough available seats for " + event.getEventName() + " (" + normalizedType + ").");
            }

            updateEventCapacity(event, normalizedType, item.getQuantity());
            eventRepository.save(event);

            TicketEntity ticket = getOrCreateTicket(event, normalizedType, getPriceByType(event, normalizedType), getAvailableSeats(event, normalizedType));
            ticket.setAvailableSeats(getAvailableSeats(event, normalizedType));
            ticketRepository.save(ticket);

            for (int i = 0; i < item.getQuantity(); i++) {
                BookingEntity booking = BookingEntity.builder()
                        .bookingTime(LocalDateTime.now())
                        .status("CONFIRMED")
                        .ticketLifecycleStatus("CONFIRMED")
                        .user(user)
                        .ticket(ticket)
                        .build();
                savedBookings.add(bookingRepository.save(booking));
            }
        }

        clearCart(session);
        return savedBookings;
    }

    public List<BookingEntity> getUserBookings(Long userId) {
        List<BookingEntity> list = bookingRepository.findByUserIdOrderByBookingTimeDesc(userId);
        for (BookingEntity booking : list) {
            syncTicketLifecycleStatus(booking);
        }
        return list;
    }

    private void syncTicketLifecycleStatus(BookingEntity booking) {
        if (booking.getTicket() == null || booking.getTicket().getEvent() == null) {
            return;
        }
        TicketEntity ticket = booking.getTicket();
        EventEntity event = ticket.getEvent();
        if (eventScheduleService.isEventFinished(event)) {
            boolean dirty = false;
            if (!"EXPIRED".equals(booking.getTicketLifecycleStatus())) {
                booking.setTicketLifecycleStatus("EXPIRED");
                dirty = true;
            }
            if (!"EXPIRED".equals(ticket.getLifecycleStatus())) {
                ticket.setLifecycleStatus("EXPIRED");
                dirty = true;
            }
            if (dirty) {
                bookingRepository.save(booking);
                ticketRepository.save(ticket);
            }
        }
    }

    private void requireBookingAllowed(EventEntity event) {
        if (!eventScheduleService.isOpenForBooking(event)) {
            throw new IllegalArgumentException(eventScheduleService.getBookingClosedMessage(event));
        }
    }

    private int validateQuantity(EventEntity event, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        if (quantity > event.getTicketsPerUser()) {
            throw new IllegalArgumentException("Maximum tickets per user is " + event.getTicketsPerUser() + " for this event.");
        }
        return quantity;
    }

    private String normalizeTicketType(String ticketType) {
        if (ticketType == null || ticketType.isBlank()) {
            throw new IllegalArgumentException("Please select a ticket type.");
        }

        String normalized = ticketType.trim().toUpperCase(Locale.ROOT);
        if (!normalized.equals("VIP") && !normalized.equals("PREMIUM") && !normalized.equals("STANDARD")) {
            throw new IllegalArgumentException("Invalid ticket type selected.");
        }
        return normalized;
    }

    private double getPriceByType(EventEntity event, String ticketType) {
        return switch (ticketType) {
            case "VIP" -> event.getVipPrice();
            case "PREMIUM" -> event.getPremiumPrice();
            case "STANDARD" -> event.getStandardPrice();
            default -> throw new IllegalArgumentException("Invalid ticket type selected.");
        };
    }

    private int getAvailableSeats(EventEntity event, String ticketType) {
        return switch (ticketType) {
            case "VIP" -> event.getVipCapacity();
            case "PREMIUM" -> event.getPremiumCapacity();
            case "STANDARD" -> event.getStandardCapacity();
            default -> throw new IllegalArgumentException("Invalid ticket type selected.");
        };
    }

    private void updateEventCapacity(EventEntity event, String ticketType, int quantity) {
        switch (ticketType) {
            case "VIP" -> event.setVipCapacity(event.getVipCapacity() - quantity);
            case "PREMIUM" -> event.setPremiumCapacity(event.getPremiumCapacity() - quantity);
            case "STANDARD" -> event.setStandardCapacity(event.getStandardCapacity() - quantity);
            default -> throw new IllegalArgumentException("Invalid ticket type selected.");
        }
    }

    private TicketEntity getOrCreateTicket(EventEntity event, String ticketType, double price, int availableSeats) {
        return ticketRepository.findByEventIdAndTypeIgnoreCase(event.getId(), ticketType)
                .orElseGet(() -> TicketEntity.builder()
                        .event(event)
                        .type(ticketType)
                        .price(price)
                        .availableSeats(availableSeats)
                        .lifecycleStatus("ACTIVE")
                        .build());
    }

    //used in admin controller
    public long getTotalTicketCount() {
        return bookingRepository.count();
    }



    public void updateCartItem(HttpSession session, Long eventId, String oldTicketType, String newTicketType, Integer newQuantity) {
        String normalizedOld = normalizeTicketType(oldTicketType);
        String normalizedNew = normalizeTicketType(newTicketType);
        if (newQuantity == null || newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        EventEntity event = getEventById(eventId);
        requireBookingAllowed(event);
        if (newQuantity > event.getTicketsPerUser()) {
            throw new IllegalArgumentException("Maximum tickets per user is " + event.getTicketsPerUser() + " for this event.");
        }
        int availableSeats = getAvailableSeats(event, normalizedNew);
        if (newQuantity > availableSeats) {
            throw new IllegalArgumentException("Only " + availableSeats + " seats available for " + normalizedNew + ".");
        }

        List<CartItemDto> cartItems = getCartItems(session);
        cartItems.removeIf(item -> item.getEventId().equals(eventId) && item.getTicketType().equalsIgnoreCase(normalizedOld));

        Optional<CartItemDto> existing = cartItems.stream()
                .filter(item -> item.getEventId().equals(eventId) && item.getTicketType().equalsIgnoreCase(normalizedNew))
                .findFirst();

        if (existing.isPresent()) {
            CartItemDto item = existing.get();
            item.setQuantity(newQuantity);
            item.setTotalPrice(newQuantity * item.getUnitPrice());
        } else {
            double price = getPriceByType(event, normalizedNew);
            cartItems.add(CartItemDto.builder()
                    .eventId(eventId)
                    .eventName(event.getEventName())
                    .ticketType(normalizedNew)
                    .quantity(newQuantity)
                    .unitPrice(price)
                    .totalPrice(price * newQuantity)
                    .build());
        }

        session.setAttribute(CART_SESSION_KEY, cartItems);
    }

    public void removeCartItem(HttpSession session, Long eventId, String ticketType) {
        String normalized = normalizeTicketType(ticketType);
        List<CartItemDto> cartItems = getCartItems(session);
        boolean removed = cartItems.removeIf(item -> item.getEventId().equals(eventId) && item.getTicketType().equalsIgnoreCase(normalized));
        if (!removed) {
            throw new IllegalArgumentException("Item not found in cart.");
        }
        session.setAttribute(CART_SESSION_KEY, cartItems);
    }
}