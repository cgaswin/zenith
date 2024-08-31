package org.user.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.user.model.CoachingRelationship;
import org.user.user.repository.CoachingRelationshipRepository;
import org.user.user.service.CoachingRelationshipService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachingRelationshipServiceImpl implements CoachingRelationshipService {

    private final CoachingRelationshipRepository coachingRelationshipRepository;

    @Autowired
    public CoachingRelationshipServiceImpl(CoachingRelationshipRepository coachingRelationshipRepository){
        this.coachingRelationshipRepository = coachingRelationshipRepository;
    }

    @Override
    public CoachingRelationship createCoachingRelationship(CoachingRelationship coachingRelationship) {
        return coachingRelationshipRepository.save(coachingRelationship);
    }

    @Override
    public Optional<CoachingRelationship> getCoachingRelationshipById(String id) {
        return coachingRelationshipRepository.findById(id);
    }

    @Override
    public List<CoachingRelationship> getAllCoachingRelationships() {
        return coachingRelationshipRepository.findAll();
    }

    @Override
    public Optional<List<CoachingRelationship>> getCoachingRelationshipsByCoachId(String coachId) {
        return coachingRelationshipRepository.findByCoachId(coachId);
    }

    @Override
    public Optional<CoachingRelationship> getCoachingRelationshipByAthleteId(String athleteId) {
        return coachingRelationshipRepository.findByAthleteId(athleteId);
    }
}
