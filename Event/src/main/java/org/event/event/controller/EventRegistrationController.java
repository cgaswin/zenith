package org.event.event.controller;

import org.event.event.dto.EventRegistrationRequestDTO;
import org.event.event.dto.EventRegistrationResponseDTO;
import org.event.event.dto.ResponseDTO;
import org.event.event.exceptions.EventItemNotFoundException;
import org.event.event.exceptions.EventNotFoundException;
import org.event.event.exceptions.EventRegistrationNotFoundException;
import org.event.event.mappers.EventRegistrationMapper;
import org.event.event.model.Event;
import org.event.event.model.EventItem;
import org.event.event.model.EventRegistration;
import org.event.event.service.impl.EventItemServiceImpl;
import org.event.event.service.impl.EventRegistrationServiceImpl;
import org.event.event.service.impl.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.event.event.model.EventRegistration.Status;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/register")
public class EventRegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(EventRegistrationController.class);
    private final EventRegistrationServiceImpl eventRegistrationService;
    private final EventRegistrationMapper eventRegistrationMapper;
    private final EventServiceImpl eventService;
    private final EventItemServiceImpl eventItemService;

    @Autowired
    public EventRegistrationController(EventRegistrationServiceImpl eventRegistrationService,
                                       EventRegistrationMapper eventRegistrationMapper,EventServiceImpl eventService,EventItemServiceImpl eventItemService) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventRegistrationMapper = eventRegistrationMapper;
        this.eventService=eventService;
        this.eventItemService=eventItemService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<EventRegistrationResponseDTO>> createEventRegistration(
            @RequestBody EventRegistrationRequestDTO requestDTO) {
        logger.info("Received request to create event registration: {}", requestDTO);

        Event event = eventService.getEventById(requestDTO.getEventId())
                .orElseThrow(() -> {
                    logger.warn("Event not found with id: {}", requestDTO.getEventId());
                    return new EventNotFoundException("Event not found with id: " + requestDTO.getEventId());
                });

        EventItem eventItem = eventItemService.getEventItemById(requestDTO.getEventItemId())
                .orElseThrow(() -> {
                    logger.warn("Event item not found with id: {}", requestDTO.getEventItemId());
                    return new EventItemNotFoundException("Event item not found with id: " + requestDTO.getEventItemId());
                });

        EventRegistration eventRegistration = eventRegistrationMapper.eventRegistrationRequestDtoToEventRegistration(requestDTO);
        eventRegistration.setEvent(event);
        eventRegistration.setEventItem(eventItem);

        System.out.println(eventRegistration);


        EventRegistration createdEventRegistration = eventRegistrationService.createEventRegistration(eventRegistration);


        EventRegistrationResponseDTO responseDTO = eventRegistrationMapper.eventRegistrationToEventRegistrationResponseDTO(createdEventRegistration);

        logger.info("Event registration created successfully: {}", responseDTO);
        ResponseDTO<EventRegistrationResponseDTO> response = new ResponseDTO<>("Event registration created successfully", true, responseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventRegistrationResponseDTO>> getEventRegistrationById(@PathVariable UUID id) {
        logger.info("Received request to get event registration by id: {}", id);

        EventRegistration eventRegistration = eventRegistrationService.getEventRegistrationById(id)
                .orElseThrow(() -> {
                    logger.warn("Event registration not found with id: {}", id);
                    return new EventRegistrationNotFoundException("Event registration not found with id: " + id);
                });

        EventRegistrationResponseDTO responseDTO = eventRegistrationMapper.eventRegistrationToEventRegistrationResponseDTO(eventRegistration);
        logger.info("Retrieved event registration: {}", responseDTO);

        ResponseDTO<EventRegistrationResponseDTO> response = new ResponseDTO<>("Event registration retrieved successfully", true, responseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getAllEventRegistrations() {
        logger.info("Received request to get all event registrations");

        List<EventRegistration> eventRegistrations = eventRegistrationService.getAllEventRegistrations();


        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(eventRegistrations);

        logger.info("Retrieved {} event registrations", eventRegistrations.size());

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("All event registrations retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByEventId(@PathVariable UUID eventId) {
        logger.info("Received request to get registrations for event id: {}", eventId);

        List<EventRegistration> eventRegistrations = eventRegistrationService.getRegistrationsByEventId(eventId)
                .orElseThrow(() -> {
                    logger.warn("No registrations found for event id: {}", eventId);
                    return new EventRegistrationNotFoundException("No registrations found for event id: " + eventId);
                });


        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(eventRegistrations);

        logger.info("Retrieved {} registrations for event id: {}", eventRegistrations.size(), eventId);

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Registrations for event retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eventItem/{eventItemId}")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByEventItemId(@PathVariable UUID eventItemId) {
        logger.info("Received request to get registrations for event item id: {}", eventItemId);

        List<EventRegistration> eventRegistrations = eventRegistrationService.getRegistrationsByEventItemId(eventItemId)
                .orElseThrow(() -> {
                    logger.warn("No registrations found for event item id: {}", eventItemId);
                    return new EventRegistrationNotFoundException("No registrations found for event item id: " + eventItemId);
                });


        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(eventRegistrations);

        logger.info("Retrieved {} registrations for event item id: {}", eventRegistrations.size(), eventItemId);

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Registrations for event item retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<EventRegistrationResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @RequestParam Status status) {
        logger.info("Received request to update status for event registration ID: {}", id);

        EventRegistration existingRegistration = eventRegistrationService.getEventRegistrationById(id)
                .orElseThrow(() -> new EventRegistrationNotFoundException("Event registration not found with id: " + id));


        existingRegistration.setStatus(status);
        EventRegistration updatedEventRegistration = eventRegistrationService.createEventRegistration(existingRegistration);

        EventRegistrationResponseDTO responseDTO = eventRegistrationMapper.eventRegistrationToEventRegistrationResponseDTO(updatedEventRegistration);

        logger.info("Event registration status updated successfully: {}", responseDTO);
        ResponseDTO<EventRegistrationResponseDTO> response = new ResponseDTO<>("Registration status updated successfully", true, responseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
