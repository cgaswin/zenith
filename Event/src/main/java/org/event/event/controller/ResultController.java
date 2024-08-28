package org.event.event.controller;

import org.event.event.dto.ResponseDTO;
import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {

    private final ResultServiceImpl resultService;
    private final EventServiceImpl eventService;
    private final EventItemServiceImpl eventItemService;
    private final ResultMapper resultMapper;

    private static final Logger logger = LoggerFactory.getLogger(ResultController.class);

    @Autowired
    public ResultController(ResultServiceImpl resultService, EventServiceImpl eventService,
                            EventItemServiceImpl eventItemService, ResultMapper resultMapper) {
        this.resultService = resultService;
        this.eventService = eventService;
        this.eventItemService = eventItemService;
        this.resultMapper = resultMapper;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ResultResponseDTO>> createResult(@RequestBody ResultRequestDTO resultRequestDTO) {
        logger.info("Received request to create result: {}", resultRequestDTO);


        Event event = eventService.getEventById(resultRequestDTO.getEventId())
                .orElseThrow(() -> {
                    logger.warn("Event not found with id: {}", resultRequestDTO.getEventId());
                    return new EventNotFoundException("Event not found with id: " + resultRequestDTO.getEventId());
                });


        EventItem eventItem = eventItemService.getEventItemById(resultRequestDTO.getEventItemId())
                .orElseThrow(() -> {
                    logger.warn("Event item not found with id: {}", resultRequestDTO.getEventItemId());
                    return new EventItemNotFoundException("Event item not found with id: " + resultRequestDTO.getEventItemId());
                });


        Result result = resultMapper.resultDtoToResultEntity(resultRequestDTO);
        result.setEvent(event);
        result.setEventItem(eventItem);
        logger.info("Result = {}",result);


        Result createdResult = resultService.createResult(result);

        logger.info("created Result = {} ",createdResult);
        ResultResponseDTO resultResponseDTO = resultMapper.resultEntityToResponseDTO(createdResult);

        logger.info("Result created successfully: {}", resultResponseDTO);
        ResponseDTO<ResultResponseDTO> response = new ResponseDTO<>("Result created successfully", true, resultResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getAllResults() {
        logger.info("Fetching all results");
        List<Result> results = resultService.getAllResults();
        List<ResultResponseDTO> resultResponseDTOs = resultMapper.resultsToResultResponseDTO(results);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ResultResponseDTO>> getResultById(@PathVariable UUID id) {
        logger.info("Fetching result with id: {}", id);
        Result result = resultService.getResultById(id)
                .orElseThrow(() -> {
                    logger.warn("Result not found with id: {}", id);
                    return new ResultNotFoundException("Result not found with id: " + id);
                });
        ResultResponseDTO resultResponseDTO = resultMapper.resultEntityToResponseDTO(result);
        ResponseDTO<ResultResponseDTO> response = new ResponseDTO<>("Result fetched successfully", true, resultResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getResultsByEventId(@PathVariable UUID eventId) {
        logger.info("Fetching results for event with id: {}", eventId);
        List<Result> results = resultService.getResultsByEventId(eventId)
                .orElseThrow(() -> {
                    logger.warn("No results found for event id: {}", eventId);
                    return new ResultNotFoundException("No results found for event ID: " + eventId);
                });
        List<ResultResponseDTO> resultResponseDTOs = resultMapper.resultsToResultResponseDTO(results);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/event-item/{eventItemId}")
    public ResponseEntity<ResponseDTO<List<ResultResponseDTO>>> getResultsByEventItemId(@PathVariable UUID eventItemId) {
        logger.info("Fetching results for event item with id: {}", eventItemId);
        List<Result> results = resultService.getResultsByEventItemId(eventItemId)
                .orElseThrow(() -> {
                    logger.warn("No results found for event item id: {}", eventItemId);
                    return new ResultNotFoundException("No results found for event item ID: " + eventItemId);
                });
        List<ResultResponseDTO> resultResponseDTOs = resultMapper.resultsToResultResponseDTO(results);
        ResponseDTO<List<ResultResponseDTO>> response = new ResponseDTO<>("Results fetched successfully", true, resultResponseDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
