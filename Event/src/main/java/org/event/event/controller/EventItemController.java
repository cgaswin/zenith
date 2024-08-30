package org.event.event.controller;

import jakarta.validation.Valid;
import org.event.event.commons.utils.JwtDecoder;
import org.event.event.dto.EventItemRequestDTO;
import org.event.event.dto.EventItemResponseDTO;

import org.event.event.dto.JwtPayloadDTO;
import org.event.event.dto.ResponseDTO;
import org.event.event.exceptions.AuthorizationException;
import org.event.event.exceptions.EventItemNotFoundException;
import org.event.event.exceptions.EventNotFoundException;
import org.event.event.mappers.EventItemMapper;
import org.event.event.model.Event;
import org.event.event.model.EventItem;
import org.event.event.service.impl.EventItemServiceImpl;
import org.event.event.service.impl.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/eventItem")
public class EventItemController {

    private static final Logger logger = LoggerFactory.getLogger(EventItemController.class);
    private final EventItemServiceImpl eventItemService;
    private final EventItemMapper eventItemMapper;
    private final EventServiceImpl eventService;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public EventItemController(EventItemServiceImpl eventItemService, EventItemMapper eventItemMapper, EventServiceImpl eventService, JwtDecoder jwtDecoder) {
        this.eventItemService = eventItemService;
        this.eventItemMapper = eventItemMapper;
        this.eventService = eventService;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<EventItemResponseDTO>> createEventItem( @RequestBody EventItemRequestDTO eventItemDTO,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to create event: {}", eventItemDTO);
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
        eventItemDTO.setCreatedBy(UUID.fromString(credentials.getUserId()));
        Event event = eventService.getEventById(eventItemDTO.getEventId())
                .orElseThrow(() -> {
                    logger.warn("Event not found with id: {}",eventItemDTO.getEventId());
                    return new EventNotFoundException("Event not found with id: " + eventItemDTO.getEventId());
                });

        EventItem eventItem = eventItemMapper.eventItemRequestDtoToEventItem(eventItemDTO);
        eventItem.setEvent(event);

        EventItem createdEventItem = eventItemService.createEventItem(eventItem);
        EventItemResponseDTO createdEventItemDTO = eventItemMapper.eventItemToEventItemResponseDTO(createdEventItem);
        logger.info("Event item created successfully: {}", createdEventItemDTO);
        ResponseDTO<EventItemResponseDTO> response = new ResponseDTO<>("Event item created successfully", true, createdEventItemDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   @GetMapping
    public ResponseEntity<ResponseDTO<List<EventItemResponseDTO>>> getAllEventItems(){
       logger.info("Received request to get all event items");
       List<EventItem> eventItems = eventItemService.getAllEventItems();
       List<EventItemResponseDTO> eventItemDTOs = eventItems.stream()
               .map(eventItemMapper::eventItemToEventItemResponseDTO)
               .collect(Collectors.toList());
       logger.info("Retrieved {} event items", eventItemDTOs.size());
       ResponseDTO<List<EventItemResponseDTO>> response = new ResponseDTO<>("All event items retrieved successfully", true, eventItemDTOs);
       return ResponseEntity.ok(response);
   }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventItemResponseDTO>> getEventItemById(@PathVariable UUID id) {
        logger.info("Received request to get event item by id: {}", id);
        EventItem eventItem = eventItemService.getEventItemById(id)
                .orElseThrow(() -> {
                    logger.warn("Event item not found with id: {}", id);
                    return new EventItemNotFoundException("Event item not found with id: " + id);
                });
        EventItemResponseDTO eventItemDTO = eventItemMapper.eventItemToEventItemResponseDTO(eventItem);
        logger.info("Retrieved event item: {}", eventItemDTO);
        ResponseDTO<EventItemResponseDTO> response = new ResponseDTO<>("Event item retrieved successfully", true, eventItemDTO);
        return ResponseEntity.ok(response);
    }


}
