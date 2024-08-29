package org.user.user.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user.user.dto.CoachRequestDTO;
import org.user.user.dto.CoachResponseDTO;
import org.user.user.dto.ResponseDTO;
import org.user.user.exception.CoachNotFoundException;
import org.user.user.mapper.CoachMapper;
import org.user.user.model.Coach;
import org.user.user.service.impl.CoachServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coach")
public class CoachController {
    private static final Logger logger = LoggerFactory.getLogger(CoachController.class);

    private final CoachServiceImpl coachService;
    private final CoachMapper coachMapper;

    @Autowired
    public CoachController(CoachServiceImpl coachService, CoachMapper coachMapper) {
        this.coachService = coachService;
        this.coachMapper = coachMapper;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> createCoach(@Valid @RequestBody CoachRequestDTO coachRequestDTO) {
        logger.info("Received request to create coach: {}", coachRequestDTO);
        Coach coach = coachMapper.coachRequestDtoToCoach(coachRequestDTO);
        logger.info("coach = {}",coach);
        Coach createdCoach = coachService.createCoach(coach);
        CoachResponseDTO coachResponseDTO = coachMapper.coachToCoachResponseDto(createdCoach);
        logger.info("Coach created successfully: {}", coachResponseDTO);
        ResponseDTO<CoachResponseDTO> response = new ResponseDTO<>("Coach created successfully", true, coachResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> getCoachById(@PathVariable UUID id) {
        logger.info("Received request to get coach with id: {}", id);
        Optional<Coach> coachOptional = coachService.getCoachById(id);
        if (coachOptional.isEmpty()) {
            logger.warn("Coach not found with id: {}", id);
            throw new CoachNotFoundException("Coach not found with id: " + id);
        }
        CoachResponseDTO coachResponseDTO = coachMapper.coachToCoachResponseDto(coachOptional.get());
        logger.info("Coach found: {}", coachResponseDTO);
        ResponseDTO<CoachResponseDTO> response = new ResponseDTO<>("Coach found", true, coachResponseDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/accepting-requests")
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> updateAcceptingRequests(
            @PathVariable UUID id,
            @RequestParam boolean acceptingRequests) {
        logger.info("Received request to update accepting requests for coach with id: {} to {}", id, acceptingRequests);
        Optional<Coach> updatedCoachOptional = coachService.updateAcceptingRequests(id, acceptingRequests);
        if (updatedCoachOptional.isEmpty()) {
            logger.warn("Coach not found with id: {}", id);
            throw new CoachNotFoundException("Coach not found with id: " + id);
        }
        CoachResponseDTO coachResponseDTO = coachMapper.coachToCoachResponseDto(updatedCoachOptional.get());
        logger.info("Coach accepting requests updated successfully: {}", coachResponseDTO);
        ResponseDTO<CoachResponseDTO> response = new ResponseDTO<>("Coach accepting requests updated successfully", true, coachResponseDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> updateCoachDetails(
            @PathVariable UUID id,
            @Valid @RequestBody CoachRequestDTO coachRequestDTO) {
        logger.info("Received request to update coach details for coach with id: {} with details: {}", id, coachRequestDTO);
        Coach coach = coachMapper.coachRequestDtoToCoach(coachRequestDTO);
        Optional<Coach> updatedCoachOptional = coachService.updateCoachDetails(id, coach);
        if (updatedCoachOptional.isEmpty()) {
            logger.warn("Coach not found with id: {}", id);
            throw new CoachNotFoundException("Coach not found with id: " + id);
        }
        CoachResponseDTO coachResponseDTO = coachMapper.coachToCoachResponseDto(updatedCoachOptional.get());
        logger.info("Coach details updated successfully: {}", coachResponseDTO);
        ResponseDTO<CoachResponseDTO> response = new ResponseDTO<>("Coach details updated successfully", true, coachResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CoachResponseDTO>>> getAllCoaches() {
        logger.info("Received request to get all coaches");
        List<Coach> coaches = coachService.getAllCoaches();
        List<CoachResponseDTO> coachResponseDTOs = coachMapper.coachesToCoachResponseDto(coaches);
        logger.info("Retrieved coaches successfully, total elements: {}", coaches.size());
        ResponseDTO<List<CoachResponseDTO>> response = new ResponseDTO<>("Coaches retrieved successfully", true, coachResponseDTOs);
        return ResponseEntity.ok(response);
    }
}
