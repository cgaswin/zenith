package org.event.event.controller;

import org.event.event.commons.utils.JwtDecoder;
import org.event.event.dto.EventRegistrationRequestDTO;
import org.event.event.dto.EventRegistrationResponseDTO;
import org.event.event.dto.JwtPayloadDTO;
import org.event.event.dto.ResponseDTO;
import org.event.event.exceptions.AuthorizationException;
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


@RestController
@RequestMapping("/api/v1/registration")
public class EventRegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(EventRegistrationController.class);
    private final EventRegistrationServiceImpl eventRegistrationService;
    private final EventRegistrationMapper eventRegistrationMapper;
    private final EventServiceImpl eventService;
    private final EventItemServiceImpl eventItemService;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public EventRegistrationController(EventRegistrationServiceImpl eventRegistrationService,
                                       EventRegistrationMapper eventRegistrationMapper, EventServiceImpl eventService, EventItemServiceImpl eventItemService, JwtDecoder jwtDecoder) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventRegistrationMapper = eventRegistrationMapper;
        this.eventService=eventService;
        this.eventItemService=eventItemService;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<EventRegistrationResponseDTO>> createEventRegistration(
            @RequestBody EventRegistrationRequestDTO requestDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        logger.info("Received request to create event: {}", requestDTO);

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
        if(!"ATHLETE".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }


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

        boolean alreadyRegistered = eventRegistrationService.isAthleteRegisteredForEventItem(
                requestDTO.getAthleteId(), requestDTO.getEventItemId());

        if (alreadyRegistered) {
            return new ResponseEntity<>(new ResponseDTO<>("Athlete is already registered for this event item", false, null),
                    HttpStatus.BAD_REQUEST);
        }


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
    public ResponseEntity<ResponseDTO<EventRegistrationResponseDTO>> getEventRegistrationById(@PathVariable String id) {
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
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByEventId(@PathVariable String eventId) {
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
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByEventItemId(@PathVariable String eventItemId) {
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
            @PathVariable String id,
            @RequestParam EventRegistration.Status status,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        // Authorization check (as you had before)
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new AuthorizationException("Authorization header is missing");
        }

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        if (token.isEmpty()) {
            throw new AuthorizationException("Authorization token is missing");
        }

        JwtPayloadDTO credentials = jwtDecoder.decodeJwt(token);
        if (!"ADMIN".equalsIgnoreCase(credentials.getRole())) {
            throw new AuthorizationException("You do not have permission to access");
        }

        EventRegistration updatedRegistration = eventRegistrationService.updateRegistrationStatus(id, status);
        EventRegistrationResponseDTO responseDTO = eventRegistrationMapper.eventRegistrationToEventRegistrationResponseDTO(updatedRegistration);

        ResponseDTO<EventRegistrationResponseDTO> response = new ResponseDTO<>("Registration status updated successfully", true, responseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByEventAndAthlete(
            @PathVariable String eventId,
            @PathVariable String athleteId) {
        logger.info("Received request to get registrations for event id: {} and athlete id: {}", eventId, athleteId);

        List<EventRegistration> eventRegistrations = eventRegistrationService.getRegistrationsByEventAndAthlete(eventId, athleteId)
                .orElseThrow(() -> {
                    logger.warn("No registrations found for event id: {} and athlete id: {}", eventId, athleteId);
                    return new EventRegistrationNotFoundException("No registrations found for event id: " + eventId + " and athlete id: " + athleteId);
                });

        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(eventRegistrations);

        logger.info("Retrieved {} registrations for event id: {} and athlete id: {}", eventRegistrations.size(), eventId, athleteId);

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Registrations for event and athlete retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getPendingRegistrations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Add authorization check for admin here
        JwtPayloadDTO credentials = jwtDecoder.decodeJwt(authorizationHeader.substring(7));
        if (!"ADMIN".equalsIgnoreCase(credentials.getRole())) {
            throw new AuthorizationException("You do not have permission to access");
        }

        List<EventRegistration> pendingRegistrations = eventRegistrationService.getPendingRegistrations();
        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(pendingRegistrations);

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Pending registrations retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approved/event-item/{eventItemId}")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getApprovedRegistrationsByEventItemId(@PathVariable String eventItemId) {
        List<EventRegistration> approvedRegistrations = eventRegistrationService.getApprovedRegistrationsByEventItemId(eventItemId);
        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(approvedRegistrations);
        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Approved registrations retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<List<EventRegistrationResponseDTO>>> getRegistrationsByAthleteId(@PathVariable String athleteId) {
        logger.info("Received request to get registrations for athlete id: {}", athleteId);

        List<EventRegistration> eventRegistrations = eventRegistrationService.getRegistrationsByAthleteId(athleteId)
                .orElseThrow(() -> {
                    logger.warn("No registrations found for athlete id: {}", athleteId);
                    return new EventRegistrationNotFoundException("No registrations found for athlete id: " + athleteId);
                });

        List<EventRegistrationResponseDTO> responseDTOs = eventRegistrationMapper.eventRegistrationsToEventRegistrationResponseDTOs(eventRegistrations);

        logger.info("Retrieved {} registrations for athlete id: {}", eventRegistrations.size(), athleteId);

        ResponseDTO<List<EventRegistrationResponseDTO>> response = new ResponseDTO<>("Registrations for athlete retrieved successfully", true, responseDTOs);
        return ResponseEntity.ok(response);
    }

}
