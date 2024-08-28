package org.event.event.service.impl;

import org.event.event.model.Event;
import org.event.event.repository.EventRepository;
import org.event.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getEventsByVenue(String venue) {
        return eventRepository.findByVenue(venue);
    }

    @Override
    public List<Event> getEventsByName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Event> getEventsCreatedBy(UUID createdBy) {
        return eventRepository.findByCreatedBy(createdBy);
    }

    @Override
    public List<Event> listUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> event.getDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> listPastEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> event.getDate().isBefore(now))
                .collect(Collectors.toList());
    }
}
