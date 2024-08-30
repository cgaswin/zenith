package org.user.user.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user.user.commons.utils.JwtDecoder;
import org.user.user.dto.AthleteRequestDTO;
import org.user.user.dto.AthleteResponseDTO;
import org.user.user.dto.JwtPayloadDTO;
import org.user.user.dto.ResponseDTO;
import org.user.user.exception.AthleteNotFoundException;
import org.user.user.exception.AuthorizationException;
import org.user.user.mapper.AthleteMapper;
import org.user.user.model.Athlete;
import org.user.user.service.impl.AthleteServiceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/athlete")
public class AthleteController {

    private static final Logger logger = LoggerFactory.getLogger(AthleteController.class);
    private final AthleteServiceImpl athleteService;
    private final AthleteMapper athleteMapper;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public AthleteController(AthleteServiceImpl athleteService, AthleteMapper athleteMapper, JwtDecoder jwtDecoder) {
        this.athleteService = athleteService;
        this.athleteMapper = athleteMapper;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AthleteResponseDTO>> createAthlete(@RequestBody AthleteRequestDTO athleteRequest ,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to create athlete: {}", athleteRequest);
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

        athleteRequest.setUserId(UUID.fromString(credentials.getUserId()));
        Athlete athlete = athleteMapper.athleteRequestDtoToAthlete(athleteRequest);
        Athlete createdAthlete = athleteService.createAthlete(athlete);
        AthleteResponseDTO createdAthleteDTO = athleteMapper.athleteToAthleteResponseDTO(createdAthlete);
        logger.info("Athlete created successfully: {}", createdAthleteDTO);
        ResponseDTO<AthleteResponseDTO> response = new ResponseDTO<>("Athlete created successfully", true, createdAthleteDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AthleteResponseDTO>>> getAllAthletes() {
        logger.info("Received request to get all athletes");
        List<Athlete> athletes = athleteService.getAllAthletes();
        List<AthleteResponseDTO> athleteDTOs = athletes.stream()
                .map(athleteMapper::athleteToAthleteResponseDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} athletes", athleteDTOs.size());
        ResponseDTO<List<AthleteResponseDTO>> response = new ResponseDTO<>("All athletes retrieved successfully", true, athleteDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AthleteResponseDTO>> getAthleteById(@PathVariable UUID id) {
        logger.info("Received request to get athlete by id: {}", id);
        Athlete athlete = athleteService.getAthleteById(id)
                .orElseThrow(() -> {
                    logger.warn("Athlete not found with id: {}", id);
                    return new AthleteNotFoundException("Athlete not found with id: " + id);
                });
        AthleteResponseDTO athleteDTO = athleteMapper.athleteToAthleteResponseDTO(athlete);
        logger.info("Retrieved athlete: {}", athleteDTO);
        ResponseDTO<AthleteResponseDTO> response = new ResponseDTO<>("Athlete retrieved successfully", true, athleteDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AthleteResponseDTO>> updateAthlete(@PathVariable UUID id,  @RequestBody AthleteRequestDTO athleteRequestDTO,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to update athlete with id: {}", id);
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

        athleteRequestDTO.setUserId(UUID.fromString(credentials.getUserId()));
        Athlete updatedAthlete = athleteService.updateAthlete(id, athleteMapper.athleteRequestDtoToAthlete(athleteRequestDTO));
        if (updatedAthlete == null) {
            logger.warn("Athlete not found with id: {}", id);
            throw new AthleteNotFoundException("Athlete not found with id: " + id);
        }
        AthleteResponseDTO updatedAthleteDTO = athleteMapper.athleteToAthleteResponseDTO(updatedAthlete);
        logger.info("Athlete updated successfully: {}", updatedAthleteDTO);
        ResponseDTO<AthleteResponseDTO> response = new ResponseDTO<>("Athlete updated successfully", true, updatedAthleteDTO);
        return ResponseEntity.ok(response);
    }

}



