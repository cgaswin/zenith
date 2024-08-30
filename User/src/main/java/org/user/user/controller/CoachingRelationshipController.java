package org.user.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user.user.commons.utils.JwtDecoder;
import org.user.user.dto.CoachingRelationshipRequestDTO;
import org.user.user.dto.CoachingRelationshipResponseDTO;
import org.user.user.dto.JwtPayloadDTO;
import org.user.user.dto.ResponseDTO;
import org.user.user.exception.AuthorizationException;
import org.user.user.exception.CoachingRelationshipNotFoundException;
import org.user.user.mapper.CoachingRelationshipMapper;
import org.user.user.model.CoachingRelationship;
import org.user.user.service.impl.CoachingRelationshipServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/relationship")
public class CoachingRelationshipController {
    private static final Logger logger = LoggerFactory.getLogger(CoachingRelationshipController.class);

    private final CoachingRelationshipServiceImpl coachingRelationshipService;
    private final CoachingRelationshipMapper coachingRelationShipMapper;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public CoachingRelationshipController(CoachingRelationshipServiceImpl coachingRelationshipService, CoachingRelationshipMapper coachingRelationShipMapper, JwtDecoder jwtDecoder) {
        this.coachingRelationshipService = coachingRelationshipService;
        this.coachingRelationShipMapper = coachingRelationShipMapper;
        this.jwtDecoder = jwtDecoder;
    }

//    @PostMapping
//    public ResponseEntity<ResponseDTO<CoachingRelationshipResponseDTO>> createCoachingRelationship(@RequestBody CoachingRelationshipRequestDTO coachingRelationshipRequestDTO) {
//        logger.info("Received request to create coaching relationship: {}", coachingRelationshipRequestDTO);
//
//        CoachingRelationship coachingRelationship = coachingRelationShipMapper
//                .coachingRelationshipRequestDtoToCoachingRelationship(coachingRelationshipRequestDTO);
//        CoachingRelationship savedCoachingRelationship = coachingRelationshipService
//                .createCoachingRelationship(coachingRelationship);
//
//        CoachingRelationshipResponseDTO responseDTO = coachingRelationShipMapper
//                .coachingRelationshipToCoachingRelationshipResponseDto(savedCoachingRelationship);
//
//        logger.info("Coaching relationship created successfully: {}", responseDTO);
//
//        return new ResponseEntity<>(new ResponseDTO<>("Coaching relationship created successfully", true, responseDTO), HttpStatus.CREATED);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CoachingRelationshipResponseDTO>> getCoachingRelationshipById(@PathVariable UUID id,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to get coaching relationship with ID: {}", id);
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
        if( !"ADMIN".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }



        Optional<CoachingRelationship> coachingRelationshipOptional = coachingRelationshipService.getCoachingRelationshipById(id);

        if (coachingRelationshipOptional.isPresent()) {
            CoachingRelationshipResponseDTO responseDTO = coachingRelationShipMapper
                    .coachingRelationshipToCoachingRelationshipResponseDto(coachingRelationshipOptional.get());

            logger.info("Found coaching relationship: {}", responseDTO);
            return new ResponseEntity<>(new ResponseDTO<>("Coaching relationship found", true, responseDTO), HttpStatus.OK);
        } else {
            logger.warn("Coaching relationship with ID {} not found", id);
            throw new CoachingRelationshipNotFoundException("Coaching relationship with ID " + id + " not found");
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CoachingRelationshipResponseDTO>>> getAllCoachingRelationships(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to get all coaching relationships");
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



        List<CoachingRelationship> coachingRelationships = coachingRelationshipService.getAllCoachingRelationships();
        List<CoachingRelationshipResponseDTO> responseDTOList = coachingRelationShipMapper
                .coachingRelationshipListToCoachingRelationshipResponseDtoList(coachingRelationships);

        logger.info("Found {} coaching relationships", responseDTOList.size());
        return new ResponseEntity<>(new ResponseDTO<>("All coaching relationships retrieved", true, responseDTOList), HttpStatus.OK);
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<ResponseDTO<List<CoachingRelationshipResponseDTO>>> getCoachingRelationshipsByCoachId(@PathVariable UUID coachId,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to get coaching relationships for coach with ID: {}", coachId);
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
        if(!"COACH".equalsIgnoreCase(credentials.getRole())&&!"ADMIN".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }



        Optional<List<CoachingRelationship>> coachingRelationships = coachingRelationshipService.getCoachingRelationshipsByCoachId(coachId);
        if(coachingRelationships.isPresent()){
            List<CoachingRelationshipResponseDTO> responseDTOList = coachingRelationShipMapper
                    .coachingRelationshipListToCoachingRelationshipResponseDtoList(coachingRelationships.get());
            logger.info("Found {} coaching relationships for coach ID {}", responseDTOList.size(), coachId);
            return new ResponseEntity<>(new ResponseDTO<>("Coaching relationships retrieved for coach", true, responseDTOList), HttpStatus.OK);
        }else{
            throw new CoachingRelationshipNotFoundException("Coaching relationship not found");
        }



    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<ResponseDTO<CoachingRelationshipResponseDTO>> getCoachingRelationshipByAthleteId(@PathVariable UUID athleteId,@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        logger.info("Received request to get coaching relationship for athlete with ID: {}", athleteId);
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
        if(!"ADMIN".equalsIgnoreCase(credentials.getRole()) && !"COACH".equalsIgnoreCase(credentials.getRole())){
            throw new AuthorizationException("You do not have permission to access");
        }



        Optional<CoachingRelationship> coachingRelationships = coachingRelationshipService.getCoachingRelationshipByAthleteId(athleteId);

        if (coachingRelationships.isEmpty()) {
            logger.warn("No coaching relationship found for athlete ID {}", athleteId);
            return new ResponseEntity<>(new ResponseDTO<>("No coaching relationship found for athlete", false, null), HttpStatus.NOT_FOUND);
        }

        CoachingRelationship coachingRelationship = coachingRelationships.get();
        CoachingRelationshipResponseDTO responseDTO = coachingRelationShipMapper
                .coachingRelationshipToCoachingRelationshipResponseDto(coachingRelationship);

        logger.info("Found coaching relationship for athlete ID {}", athleteId);
        return new ResponseEntity<>(new ResponseDTO<>("Coaching relationship retrieved for athlete", true, responseDTO), HttpStatus.OK);
    }
}
