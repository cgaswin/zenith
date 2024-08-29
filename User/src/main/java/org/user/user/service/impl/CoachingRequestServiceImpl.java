package org.user.user.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public CoachingRequest createCoachingRequest(CoachingRequest coachingRequest) {
        Athlete athlete = athleteRepository.findById(coachingRequest.getAthlete().getId())
                .orElseThrow(() -> new AthleteNotFoundException("Athlete not found"));
        Coach coach = coachRepository.findById(coachingRequest.getCoach().getId())
                .orElseThrow(() -> new CoachNotFoundException("Coach not found"));
        Optional<CoachingRelationship> relationship = coachingRelationshipRepository.findByAthleteId(coachingRequest.getAthlete().getId());
        if(!coach.isAcceptingRequests()){
            throw new CoachNotAvailableForRequestException("Coach is not available to accept request");
        }
        if(relationship.isEmpty()){
            coachingRequest.setAthlete(athlete);
            coachingRequest.setCoach(coach);

            return coachingRequestRepository.save(coachingRequest);
        }else{
            throw new CoachAlreadyAssignedException("Coach Already Assigned");
        }

    }

    @Override
    public Optional<CoachingRequest> getCoachingRequestById(UUID id) {
        return coachingRequestRepository.findById(id);
    }

    @Override
    public List<CoachingRequest> getAllCoachingRequests() {
        return coachingRequestRepository.findAll();
    }

    @Override
    public List<CoachingRequest> getCoachingRequestsByAthleteId(UUID athleteId) {
        return  coachingRequestRepository.findByAthleteId(athleteId);
    }

    @Override
    public List<CoachingRequest> getCoachingRequestsByCoachId(UUID coachId) {
        return coachingRequestRepository.findByCoachId(coachId);
    }

    @Override
    public void deleteCoachingRequestsByAthleteId(UUID athleteId) {
        List<CoachingRequest> requests = coachingRequestRepository.findByAthleteId(athleteId);
        coachingRequestRepository.deleteAll(requests);
    }

    @Override
    @Transactional
    public boolean updateCoachingRequestStatus(UUID id, CoachingRequest.Status status) {
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
    public void createCoachingRelationshipForApprovedRequest(UUID coachingRequestId) {
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



