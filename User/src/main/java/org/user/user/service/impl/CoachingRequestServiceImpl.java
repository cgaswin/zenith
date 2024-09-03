package org.user.user.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.user.dto.CoachingRequestRequestDTO;
import org.user.user.exception.*;
import org.user.user.model.Athlete;
import org.user.user.model.Coach;
import org.user.user.model.CoachingRelationship;
import org.user.user.model.CoachingRequest;
import org.user.user.repository.AthleteRepository;
import org.user.user.repository.CoachRepository;
import org.user.user.repository.CoachingRelationshipRepository;
import org.user.user.repository.CoachingRequestRepository;
import org.user.user.service.CoachingRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachingRequestServiceImpl implements CoachingRequestService {

    private final CoachingRequestRepository coachingRequestRepository;
    private final CoachingRelationshipServiceImpl coachingRelationshipService;
    private final AthleteRepository athleteRepository;
    private final CoachRepository coachRepository;
    private final CoachingRelationshipRepository coachingRelationshipRepository;

    @Autowired
    public CoachingRequestServiceImpl(CoachRepository coachRepository,CoachingRequestRepository coachingRequestRepository,CoachingRelationshipServiceImpl coachingRelationshipService,AthleteRepository athleteRepository,CoachingRelationshipRepository coachingRelationshipRepository){
        this.coachingRequestRepository = coachingRequestRepository;
        this.coachingRelationshipService=coachingRelationshipService;
        this.coachRepository=coachRepository;
        this.athleteRepository=athleteRepository;
        this.coachingRelationshipRepository=coachingRelationshipRepository;
    }

    @Override
    public CoachingRequest createCoachingRequest(CoachingRequestRequestDTO requestDTO) {
        Athlete athlete = athleteRepository.findById(requestDTO.getAthleteId())
                .orElseThrow(() -> new AthleteNotFoundException("Athlete not found with ID: " + requestDTO.getAthleteId()));
        Coach coach = coachRepository.findById(requestDTO.getCoachId())
                .orElseThrow(() -> new CoachNotFoundException("Coach not found with ID: " + requestDTO.getCoachId()));

        if (!coach.isAcceptingRequests()) {
            throw new CoachNotAvailableForRequestException("Coach is not available to accept requests");
        }

        Optional<CoachingRelationship> relationship = coachingRelationshipRepository.findByAthleteId(athlete.getId());
        if (relationship.isPresent()) {
            throw new CoachAlreadyAssignedException("Athlete already has a coach assigned");
        }

        CoachingRequest coachingRequest = new CoachingRequest();
        coachingRequest.setAthlete(athlete);
        coachingRequest.setCoach(coach);
        coachingRequest.setStatus(CoachingRequest.Status.PENDING);
        coachingRequest.setRequestDate(LocalDateTime.now());

        return coachingRequestRepository.save(coachingRequest);
    }

    @Override
    public Optional<CoachingRequest> getCoachingRequestById(String id) {
        return coachingRequestRepository.findById(id);
    }

    @Override
    public List<CoachingRequest> getAllCoachingRequests() {
        return coachingRequestRepository.findAll();
    }

    @Override
    public List<CoachingRequest> getCoachingRequestsByAthleteId(String athleteId) {
        return  coachingRequestRepository.findByAthleteId(athleteId);
    }

    @Override
    public List<CoachingRequest> getCoachingRequestsByCoachId(String coachId) {
        return coachingRequestRepository.findByCoachId(coachId);
    }

    @Override
    public void deleteCoachingRequestsByAthleteId(String athleteId) {
        List<CoachingRequest> requests = coachingRequestRepository.findByAthleteId(athleteId);
        coachingRequestRepository.deleteAll(requests);
    }

    @Override
    @Transactional
    public boolean updateCoachingRequestStatus(String id, CoachingRequest.Status status) {
        Optional<CoachingRequest> coachingRequestOpt = coachingRequestRepository.findById(id);
        if (coachingRequestOpt.isPresent()) {
            CoachingRequest coachingRequest = coachingRequestOpt.get();
            coachingRequest.setStatus(status);
            coachingRequestRepository.save(coachingRequest);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void createCoachingRelationshipForApprovedRequest(String coachingRequestId) {
        Optional<CoachingRequest> coachingRequestOpt = coachingRequestRepository.findById(coachingRequestId);
        if (coachingRequestOpt.isPresent()) {
            CoachingRequest coachingRequest = coachingRequestOpt.get();
            if (coachingRequest.getStatus() == CoachingRequest.Status.APPROVED) {
                CoachingRelationship coachingRelationship = new CoachingRelationship();
                coachingRelationship.setAthlete(coachingRequest.getAthlete());
                coachingRelationship.setCoach(coachingRequest.getCoach());
                coachingRelationshipService.createCoachingRelationship(coachingRelationship);

                deleteCoachingRequestsByAthleteId(coachingRequest.getAthlete().getId());
            }else if (coachingRequest.getStatus() == CoachingRequest.Status.REJECTED) {
                coachingRequestRepository.delete(coachingRequest);
            }
        }
        else {
            throw new CoachingRequestNotFoundException("Coaching request not found");
        }
    }
}



