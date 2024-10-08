package org.event.event.controller;

import jakarta.validation.Valid;
import org.event.event.commons.utils.JwtDecoder;
import org.event.event.dto.*;
import org.event.event.exceptions.AuthorizationException;
import org.event.event.exceptions.EventNotFoundException;
import org.event.event.mappers.EventMapper;
import org.event.event.model.Event;
import org.event.event.model.EventItem;
import org.event.event.repository.EventRepository;
import org.event.event.service.impl.EventServiceImpl;
import org.event.event.service.impl.S3FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final EventServiceImpl eventService;
    private final EventMapper eventMapper;
    private final JwtDecoder jwtDecoder;
    private final S3FileUpload s3FileUpload;
    private final EventRepository eventRepository;


    @Autowired
    public EventController(EventServiceImpl eventService, EventMapper eventMapper, JwtDecoder jwtDecoder, S3FileUpload s3FileUpload, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.jwtDecoder = jwtDecoder;
        this.s3FileUpload = s3FileUpload;
        this.eventRepository = eventRepository;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<EventResponseDTO>> createEvent(@ModelAttribute EventRequestDTO event, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws IOException {
        logger.info("Received request to create event: {}", event);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new AuthorizationException("Authorization header is missing");
        }

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        if(token.isEmpty()){
            throw new AuthorizationException("Authorization token is missing");
        }

        JwtPayloadDTO credentials = jwtDecoder.decodeJwt(token);
        logger.info(credentials.toString());
        logger.info(credentials.getRole());
        if(!"ADMIN".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }

        String photoUrl = s3FileUpload.uploadFile(event.getImage());
        logger.info("event {}",event);

        Event createdEvent = eventService.createEvent(event.getEvent());
        createdEvent.setCreatedBy(credentials.getUserId());
        createdEvent.setPhotoUrl(photoUrl);
        eventRepository.save(createdEvent);



        EventResponseDTO createdEventDTO = eventMapper.eventToEventResponseDTO(createdEvent);
        logger.info("Event created successfully: {}", createdEventDTO);
        ResponseDTO<EventResponseDTO> response = new ResponseDTO<>("Event created successfully", true, createdEventDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EventResponseDTO>>> getEvents(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String createdBy,
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

        List<EventResponseDTO> eventDTOs = events.stream()
                .map(eventMapper::eventToEventResponseDTO)
                .collect(Collectors.toList());

        logger.info("Retrieved {} events", eventDTOs.size());
        ResponseDTO<List<EventResponseDTO>> response = new ResponseDTO<>(message, true, eventDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventResponseDTO>> getEventById(@PathVariable String id) {
        logger.info("Received request to get event by id: {}", id);
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> {
                    logger.warn("Event not found with id: {}", id);
                    return new EventNotFoundException("Event not found with id: " + id);
                });
        EventResponseDTO eventDTO = eventMapper.eventToEventResponseDTO(event);
        logger.info("Retrieved event: {}", eventDTO);
        ResponseDTO<EventResponseDTO> response = new ResponseDTO<>("Event retrieved successfully", true, eventDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<ResponseDTO<StatDTO>> getEventStats() {
        logger.info("Received request to get event statistics");
        StatDTO stats = eventService.getStats();
        ResponseDTO<StatDTO> response = new ResponseDTO<>("Event statistics retrieved successfully", true, stats);
        return ResponseEntity.ok(response);
    }



}
