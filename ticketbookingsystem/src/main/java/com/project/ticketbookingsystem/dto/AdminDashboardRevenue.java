package com.project.ticketbookingsystem.dto;

/**
 * Aggregated payment revenue for the admin dashboard (single round-trip friendly).
 */
public record AdminDashboardRevenue(
        double totalRevenue,
        double footballRevenue,
        double basketballRevenue,
        double handballRevenue,
        double othersRevenue
) {
}
