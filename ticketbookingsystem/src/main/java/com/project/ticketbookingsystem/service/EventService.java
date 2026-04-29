package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import com.project.ticketbookingsystem.model.EventEntity;
import java.util.List;
@Service
public class EventService {
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
        return eventRepository.findAll();
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
