package com.project.ticketbookingsystem.service;

import org.springframework.data.domain.Sort;
import com.project.ticketbookingsystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import com.project.ticketbookingsystem.model.EventEntity;
import java.util.List;
@Service
public class EventService {

    private static final String ALL_LABEL = "All Categories";
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Save event to database (used in add event method in controller)
    public void addEvent(EventEntity event) {
        eventRepository.save(event);
    }

    // Retrieve all events (to put list of events in dashboard used in getAllEvents method in controller)
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Filter events by category label from the public events page (matches add-event options).
     * Accepts display labels like Football or enum names like FOOTBALL.
     */
    public List<EventEntity> getEventsByCategorySelection(String categoryParam) {
        if (categoryParam == null || categoryParam.isBlank()) {
            return getAllEvents();
        }
        if (ALL_LABEL.equalsIgnoreCase(categoryParam.trim())) {
            return getAllEvents();
        }
        EventEntity.Category category = resolveCategory(categoryParam.trim());
        if (category == null) {
            return getAllEvents();
        }
        return eventRepository.findByCategory(category);
    }

    /** Label shown in the events page dropdown after selection (or All Categories). */
    public String normalizeSelectedCategoryLabel(String categoryParam) {
        if (categoryParam == null || categoryParam.isBlank()) {
            return ALL_LABEL;
        }
        String trimmed = categoryParam.trim();
        if (ALL_LABEL.equalsIgnoreCase(trimmed)) {
            return ALL_LABEL;
        }
        EventEntity.Category resolved = resolveCategory(trimmed);
        if (resolved == null) {
            return ALL_LABEL;
        }
        return switch (resolved) {
            case FOOTBALL -> "Football";
            case BASKETBALL -> "Basketball";
            case HANDBALL -> "Handball";
            case OTHERS -> "Others";
        };
    }

    private EventEntity.Category resolveCategory(String raw) {
        String upper = raw.toUpperCase();
        try {
            return EventEntity.Category.valueOf(upper);
        } catch (IllegalArgumentException ignored) {
        }
        return switch (raw) {
            case "Football" -> EventEntity.Category.FOOTBALL;
            case "Basketball" -> EventEntity.Category.BASKETBALL;
            case "Handball" -> EventEntity.Category.HANDBALL;
            case "Others", "Other" -> EventEntity.Category.OTHERS;
            default -> null;
        };
    }

    // used in update events method to determine which method is getting updated
    public EventEntity getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
    }


    public void updateEvent(EventEntity event) {
        eventRepository.save(event);
    }


    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // used to tell how many events in admin page
    public long getTotalEvents() {
        return eventRepository.count();
    }
}
