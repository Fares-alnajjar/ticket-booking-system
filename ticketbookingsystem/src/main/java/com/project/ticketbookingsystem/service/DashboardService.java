package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.AdminDashboardRevenue;
import com.project.ticketbookingsystem.repository.BookingRepository;
import com.project.ticketbookingsystem.repository.EventRepository;
import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;

    public DashboardService(EventRepository eventRepository,
                            UserRepository userRepository,
                            BookingRepository bookingRepository,
                            PaymentService paymentService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
    }
    
    @Cacheable(value = "dashboardStats", key = "'all'")
    public Map<String, Object> getAllDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts (fast queries)
        stats.put("totalEvents", eventRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalTickets", bookingRepository.count());
        
        AdminDashboardRevenue revenue = paymentService.getAdminDashboardRevenue();
        stats.put("totalRevenue", revenue.totalRevenue());
        stats.put("footballRevenue", revenue.footballRevenue());
        stats.put("basketballRevenue", revenue.basketballRevenue());
        stats.put("handballRevenue", revenue.handballRevenue());
        stats.put("othersRevenue", revenue.othersRevenue());
        
        return stats;
    }
}
