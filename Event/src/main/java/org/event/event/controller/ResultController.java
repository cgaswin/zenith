package org.event.event.controller;

import org.event.event.commons.utils.JwtDecoder;
import org.event.event.dto.JwtPayloadDTO;
import org.event.event.dto.ResponseDTO;
import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
import org.event.event.exceptions.AuthorizationException;
import org.event.event.exceptions.EventItemNotFoundException;
import org.event.event.exceptions.EventNotFoundException;
import org.event.event.exceptions.ResultNotFoundException;
import org.event.event.mappers.ResultMapper;
import org.event.event.model.Event;
import org.event.event.model.EventItem;
import org.event.event.model.Result;
import org.event.event.service.impl.EventItemServiceImpl;
import org.event.event.service.impl.EventServiceImpl;
import org.event.event.service.impl.ResultServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/result")
public class ResultController {

    private final ResultServiceImpl resultService;
    private final ResultMapper resultMapper;
    private final JwtDecoder jwtDecoder;

    private static final Logger logger = LoggerFactory.getLogger(ResultController.class);

    @Autowired
    public ResultController(ResultServiceImpl resultService, ResultMapper resultMapper, JwtDecoder jwtDecoder) {
        this.resultService = resultService;
        this.resultMapper = resultMapper;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/bulk")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> createBulkResults(
            @RequestBody List<ResultRequestDTO> resultRequests,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        logger.info("Received request to create bulk results");

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new AuthorizationException("Authorization header is missing");
        }

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        if (token.isEmpty()) {
            throw new AuthorizationException("Authorization token is missing");
        }

        JwtPayloadDTO credentials = jwtDecoder.decodeJwt(token);
        logger.info("Decoded JWT: {}", credentials);
        logger.info("User role: {}", credentials.getRole());

        if (!"ADMIN".equalsIgnoreCase(credentials.getRole())) {
            throw new AuthorizationException("You do not have permission to access this resource");
        }

        List<Result> createdResults = resultService.createBulkResults(resultRequests);
        List<ResultResponseDTO> responseDTOs = resultMapper.resultsToResultResponseDTOs(createdResults);

        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results created successfully", true, responseDTOs);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getResultsByEventId(@PathVariable String eventId) {
        logger.info("Fetching results for event with id: {}", eventId);
        List<Result> results = resultService.getResultsByEventId(eventId);
        List<ResultResponseDTO> resultResponseDTOs = resultMapper.resultsToResultResponseDTOs(results);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event-item/{eventItemId}")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getResultsByEventItemId(@PathVariable String eventItemId) {
        logger.info("Fetching results for event item with id: {}", eventItemId);
        List<Result> results = resultService.getResultsByEventItemId(eventItemId);
        List<ResultResponseDTO> resultResponseDTOs = resultMapper.resultsToResultResponseDTOs(results);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getResultsByAthleteId(@PathVariable String athleteId) {
        logger.info("Fetching results for athlete with id: {}", athleteId);
        List<ResultResponseDTO> resultResponseDTOs = resultService.getResultsByAthleteId(athleteId);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/athlete/{athleteId}/top")
    public ResponseEntity<ResponseDTO<ResultResponseDTO>> getTopPerformanceByAthleteId(@PathVariable String athleteId) {
        logger.info("Fetching top performance for athlete with id: {}", athleteId);
        try {
            ResultResponseDTO resultResponseDTO = resultService.getTopPerformanceByAthleteId(athleteId);
            ResponseDTO<ResultResponseDTO> response = new ResponseDTO<>("Top performance fetched successfully", true, resultResponseDTO);
            return ResponseEntity.ok(response);
        } catch (ResultNotFoundException e) {
            logger.error("Top performance not found for athlete with id: {}", athleteId);
            throw e;
        }
    }
}
