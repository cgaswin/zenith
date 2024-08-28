package org.event.event.controller;

import org.event.event.exceptions.EventNotFoundException;
import org.event.event.exceptions.InvalidEventDataException;
import org.event.event.dto.ResponseDTO;
import org.event.event.model.Event;
import org.event.event.service.impl.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final EventServiceImpl eventService;

    @Autowired
    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<ResponseDTO<Event>> createEvent(@RequestBody Event event) {
        logger.info("Received request to create event: {}", event);
        if (event.getName() == null || event.getName().isEmpty()) {
            logger.warn("Attempt to create event with empty name");
            throw new InvalidEventDataException("Event name cannot be empty");
        }
        Event createdEvent = eventService.createEvent(event);
        logger.info("Event created successfully: {}", createdEvent);
        ResponseDTO<Event> response = new ResponseDTO<>("Event created successfully", true, createdEvent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<ResponseDTO<List<Event>>> getEvents(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID createdBy,
            @RequestParam(required = false) Boolean upcoming
    ) {
        logger.info("Received request to get events. Params: venue={}, name={}, createdBy={}, upcoming={}", venue, name, createdBy, upcoming);
        List<Event> events;
        String message;

        if (venue != null) {
            events = eventService.getEventsByVenue(venue);
            message = "Events for venue retrieved successfully";
        } else if (name != null) {
            events = eventService.getEventsByName(name);
            message = "Events by name retrieved successfully";
        } else if (createdBy != null) {
            events = eventService.getEventsCreatedBy(createdBy);
            message = "Events by creator retrieved successfully";
        } else if (upcoming != null) {
            events = upcoming ? eventService.listUpcomingEvents() : eventService.listPastEvents();
            message = (upcoming ? "Upcoming" : "Past") + " events retrieved successfully";
        } else {
            events = eventService.getAllEvents();
            message = "All events retrieved successfully";
        }

        logger.info("Retrieved {} events", events.size());
        ResponseDTO<List<Event>> response = new ResponseDTO<>(message, true, events);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<ResponseDTO<Event>> getEventById(@PathVariable UUID id) {
        logger.info("Received request to get event by id: {}", id);
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> {
                    logger.warn("Event not found with id: {}", id);
                    return new EventNotFoundException("Event not found with id: " + id);
                });
        logger.info("Retrieved event: {}", event);
        ResponseDTO<Event> response = new ResponseDTO<>("Event retrieved successfully", true, event);
        return ResponseEntity.ok(response);
    }

}
