package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.model.BookingEntity;
import com.project.ticketbookingsystem.model.EventEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Event time window (start = date + time, end = start + duration) and derived statuses.
 */
@Service
public class EventScheduleService {

    public static final int DEFAULT_DURATION_MINUTES = 60;

    public int getDurationMinutes(EventEntity event) {
        if (event.getDurationMinutes() == null || event.getDurationMinutes() < 1) {
            return DEFAULT_DURATION_MINUTES;
        }
        return event.getDurationMinutes();
    }

    public LocalDateTime getStartDateTime(EventEntity event) {
        return LocalDateTime.of(event.getEventDate(), event.getEventTime());
    }

    public LocalDateTime getEndDateTime(EventEntity event) {
        return getStartDateTime(event).plusMinutes(getDurationMinutes(event));
    }

    public boolean isFullyBooked(EventEntity event) {
        int total = safe(event.getVipCapacity()) + safe(event.getPremiumCapacity()) + safe(event.getStandardCapacity());
        return total <= 0;
    }

    public boolean isEventFinished(EventEntity event) {
        return LocalDateTime.now().isAfter(getEndDateTime(event));
    }

    public boolean isEventStarted(EventEntity event) {
        return !LocalDateTime.now().isBefore(getStartDateTime(event));
    }

    /**
     * User may book only before the event start time, and only while seats remain and the event is not over.
     */
    public boolean isOpenForBooking(EventEntity event) {
        if (isEventFinished(event)) {
            return false;
        }
        if (isFullyBooked(event)) {
            return false;
        }
        return LocalDateTime.now().isBefore(getStartDateTime(event));
    }

    public String getBookingClosedMessage(EventEntity event) {
        if (isEventFinished(event)) {
            return "This event has ended. Booking is closed.";
        }
        if (isFullyBooked(event)) {
            return "This event is fully booked.";
        }
        if (isEventStarted(event)) {
            return "Booking is only available before the event starts.";
        }
        return "";
    }

    /**
     * Admin dashboard lifecycle: Finished &gt; Fully booked &gt; Running &gt; Scheduled.
     */
    public AdminEventStatus getAdminEventStatus(EventEntity event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = getStartDateTime(event);
        LocalDateTime end = getEndDateTime(event);
        boolean full = isFullyBooked(event);

        if (now.isAfter(end)) {
            return AdminEventStatus.FINISHED;
        }
        if (now.isBefore(start)) {
            return full ? AdminEventStatus.FULLY_BOOKED : AdminEventStatus.SCHEDULED;
        }
        if (full) {
            return AdminEventStatus.FULLY_BOOKED;
        }
        return AdminEventStatus.ACTIVE;
    }

    public String adminStatusLabel(EventEntity event) {
        return switch (getAdminEventStatus(event)) {
            case SCHEDULED -> "Scheduled";
            case ACTIVE -> "Running now";
            case FINISHED -> "Finished";
            case FULLY_BOOKED -> "Fully booked";
        };
    }

    public String adminStatusBadgeClass(EventEntity event) {
        return "badge " + switch (getAdminEventStatus(event)) {
            case SCHEDULED -> "bg-info text-dark";
            case ACTIVE -> "bg-success";
            case FINISHED -> "bg-secondary";
            case FULLY_BOOKED -> "bg-warning text-dark";
        };
    }

    /**
     * Shown to the user for a purchased ticket: Expired after event end, otherwise Confirmed.
     */
    public String getTicketLifecycleDisplay(BookingEntity booking) {
        if ("EXPIRED".equalsIgnoreCase(booking.getTicketLifecycleStatus())) {
            return "Expired";
        }
        if (booking.getTicket() != null && booking.getTicket().getEvent() != null
                && isEventFinished(booking.getTicket().getEvent())) {
            return "Expired";
        }
        return "Confirmed";
    }

    public enum AdminEventStatus {
        SCHEDULED,
        ACTIVE,
        FINISHED,
        FULLY_BOOKED
    }

    private static int safe(Integer n) {
        return n == null ? 0 : n;
    }
}
