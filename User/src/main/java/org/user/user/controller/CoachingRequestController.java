package org.user.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user.user.dto.CoachingRequestRequestDTO;
import org.user.user.dto.CoachingRequestResponseDTO;
import org.user.user.dto.ResponseDTO;
import org.user.user.exception.CoachingRequestNotFoundException;
import org.user.user.mapper.CoachingRequestMapper;
import org.user.user.model.CoachingRelationship;
import org.user.user.model.CoachingRequest;
import org.user.user.service.impl.CoachingRequestServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/request")
public class CoachingRequestController {
    private static final Logger logger = LoggerFactory.getLogger(CoachingRequestController.class);

    private final CoachingRequestServiceImpl coachingRequestService;
    private final CoachingRequestMapper coachingRequestMapper;

    public CoachingRequestController(CoachingRequestServiceImpl coachingRequestService, CoachingRequestMapper coachingRequestMapper) {
        this.coachingRequestService = coachingRequestService;
        this.coachingRequestMapper = coachingRequestMapper;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CoachingRequestResponseDTO>> createCoachingRequest(@RequestBody CoachingRequestRequestDTO coachingRequestRequestDTO) {
        logger.info("Received request to create coaching request: {}", coachingRequestRequestDTO);

        CoachingRequest coachingRequest = coachingRequestMapper.coachingRequestRequestDtoToCoachingRequest(coachingRequestRequestDTO);
        CoachingRequest createdCoachingRequest = coachingRequestService.createCoachingRequest(coachingRequest);
        CoachingRequestResponseDTO responseDTO = coachingRequestMapper.coachingRequestToCoachingRequestResponseDto(createdCoachingRequest);

        logger.info("Coaching request created with ID: {}", responseDTO.getId());
        ResponseDTO<CoachingRequestResponseDTO> response = new ResponseDTO<>("Coaching request created successfully", true, responseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CoachingRequestResponseDTO>> getCoachingRequestById(@PathVariable UUID id) {
        logger.info("Received request to get coaching request with ID: {}", id);

        CoachingRequest coachingRequest = coachingRequestService.getCoachingRequestById(id)
                .orElseThrow(() -> {
                    logger.error("Coaching request with ID {} not found", id);
                     return new CoachingRequestNotFoundException("Coaching request not found with ID: " + id);
                });

        CoachingRequestResponseDTO responseDTO = coachingRequestMapper.coachingRequestToCoachingRequestResponseDto(coachingRequest);
        ResponseDTO<CoachingRequestResponseDTO> response = new ResponseDTO<>("Coaching request retrieved successfully", true, responseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<List<CoachingRequestResponseDTO>>> getCoachingRequestsByAthleteId(@PathVariable UUID athleteId) {
        logger.info("Received request to get coaching requests for athlete with ID: {}", athleteId);

        List<CoachingRequest> coachingRequests = coachingRequestService.getCoachingRequestsByAthleteId(athleteId);
        List<CoachingRequestResponseDTO> responseDTOList = coachingRequests.stream()
                .map(coachingRequestMapper::coachingRequestToCoachingRequestResponseDto)
                .collect(Collectors.toList());

        logger.info("Found {} coaching requests for athlete ID {}", responseDTOList.size(), athleteId);
        ResponseDTO<List<CoachingRequestResponseDTO>> response = new ResponseDTO<>("Coaching requests retrieved successfully", true, responseDTOList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<ResponseDTO<List<CoachingRequestResponseDTO>>> getCoachingRequestsByCoachId(@PathVariable UUID coachId) {
        logger.info("Received request to get coaching requests for coach with ID: {}", coachId);

        List<CoachingRequest> coachingRequests = coachingRequestService.getCoachingRequestsByCoachId(coachId);
        List<CoachingRequestResponseDTO> responseDTOList = coachingRequests.stream()
                .map(coachingRequestMapper::coachingRequestToCoachingRequestResponseDto)
                .collect(Collectors.toList());

        logger.info("Found {} coaching requests for coach ID {}", responseDTOList.size(), coachId);
        ResponseDTO<List<CoachingRequestResponseDTO>> response = new ResponseDTO<>("Coaching requests retrieved successfully", true, responseDTOList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<Void>> updateCoachingRequestStatus(@PathVariable UUID id, @RequestParam CoachingRequest.Status status) {
        logger.info("Received request to update coaching request status for ID: {} to {}", id, status);

        boolean updated = coachingRequestService.updateCoachingRequestStatus(id, status);
        if (updated) {
            if (status == CoachingRequest.Status.APPROVED) {
                coachingRequestService.createCoachingRelationshipForApprovedRequest(id);
            }
            ResponseDTO<Void> response = new ResponseDTO<>("Coaching request status updated successfully", true, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {

            logger.error("Failed to update status for coaching request with ID {}", id);
            throw  new CoachingRequestNotFoundException("Coaching request not found with ID: " + id);
        }
    }

    @DeleteMapping("/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<Void>> deleteCoachingRequestsByAthleteId(@PathVariable UUID athleteId) {
        logger.info("Received request to delete coaching requests for athlete with ID: {}", athleteId);

        coachingRequestService.deleteCoachingRequestsByAthleteId(athleteId);
        ResponseDTO<Void> response = new ResponseDTO<>("Coaching requests deleted successfully", true, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
