package org.event.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.event.event.dto.StatDTO;
import org.event.event.model.Event;
import org.event.event.repository.EventRepository;
import org.event.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    public Optional<Event> getEventById(String id) {
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
    public List<Event> getEventsCreatedBy(String createdBy) {
        return eventRepository.findByCreatedBy(createdBy);
    }

    @Override
    public List<Event> listUpcomingEvents() {
        OffsetDateTime now = OffsetDateTime.now();

        return eventRepository.findAll().stream()
                .filter(event -> {
                    try {
                        OffsetDateTime eventDate = OffsetDateTime.parse(event.getDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        return eventDate.isAfter(now);
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle parsing exception appropriately
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> listPastEvents() {
        OffsetDateTime now = OffsetDateTime.now();

        return eventRepository.findAll().stream()
                .filter(event -> {
                    try {
                        OffsetDateTime eventDate = OffsetDateTime.parse(event.getDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        return eventDate.isBefore(now);
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle parsing exception appropriately
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public StatDTO getStats() {
        List<Event> events = eventRepository.findAll();
        Map<String, Integer> dailyEventCounts = new HashMap<>();
        long totalValidEvents = 0;

        for (Event event : events) {
            LocalDateTime createdAt = event.getCreatedAt();
            if (createdAt != null) {
                LocalDate eventDate = createdAt.toLocalDate();
                String dateKey = eventDate.toString();
                dailyEventCounts.put(dateKey, dailyEventCounts.getOrDefault(dateKey, 0) + 1);
                totalValidEvents++;
            } else {
                log.warn("Event with ID {} has null createdAt value", event.getId());
            }
        }

        StatDTO statDTO = new StatDTO();
        statDTO.setTotal(totalValidEvents);
        statDTO.setDailyCounts(dailyEventCounts);

        log.info("Generated stats: total events = {}, daily counts = {}", totalValidEvents, dailyEventCounts);

        return statDTO;
    }
}
