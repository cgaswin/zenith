package org.user.user.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user.user.commons.utils.JwtDecoder;
import org.user.user.dto.*;
import org.user.user.exception.AuthorizationException;
import org.user.user.exception.CoachNotFoundException;
import org.user.user.mapper.CoachMapper;
import org.user.user.model.Coach;
import org.user.user.service.impl.CoachServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/coach")
public class CoachController {
    private static final Logger logger = LoggerFactory.getLogger(CoachController.class);

    private final CoachServiceImpl coachService;
    private final CoachMapper coachMapper;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public CoachController(CoachServiceImpl coachService, CoachMapper coachMapper, JwtDecoder jwtDecoder) {
        this.coachService = coachService;
        this.coachMapper = coachMapper;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> createCoach( @RequestBody CoachRequestDTO coachRequestDTO,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to create coach: {}", coachRequestDTO);
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
        if("ATHLETE".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }

        coachRequestDTO.setUserId(credentials.getUserId());
        Coach coach = coachMapper.coachRequestDtoToCoach(coachRequestDTO);
        logger.info("coach = {}",coach);
        Coach createdCoach = coachService.createCoach(coach);
        CoachResponseDTO coachResponseDTO = coachMapper.coachToCoachResponseDto(createdCoach);
        logger.info("Coach created successfully: {}", coachResponseDTO);
        ResponseDTO<CoachResponseDTO> response = new ResponseDTO<>("Coach created successfully", true, coachResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CoachResponseDTO>> getCoachById(@PathVariable String id) {
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
            @PathVariable String id,
            @RequestParam boolean acceptingRequests,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        logger.info("Received request to update accepting requests for coach with id: {} to {}", id, acceptingRequests);
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
        if("ATHLETE".equalsIgnoreCase(credentials.getRole())){
            logger.info("the role is of athlete");
            throw new AuthorizationException("You do not have permission to access");
        }
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
            @PathVariable String id,
            @Valid @RequestBody CoachRequestDTO coachRequestDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        logger.info("Received request to update coach details for coach with id: {} with details: {}", id, coachRequestDTO);
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
        if("ATHLETE".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }

        coachRequestDTO.setUserId(credentials.getUserId());
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

    @GetMapping("/stats")
    public ResponseEntity<ResponseDTO<StatDTO>> getCoachStats() {
        logger.info("Received request to get coach statistics");
        StatDTO stats = coachService.getStats();
        ResponseDTO<StatDTO> response = new ResponseDTO<>("Coach statistics retrieved successfully", true, stats);
        return ResponseEntity.ok(response);
    }
}
