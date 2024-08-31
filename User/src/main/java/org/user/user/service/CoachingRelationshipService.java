package org.user.user.service;

import org.user.user.model.CoachingRelationship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachingRelationshipService {
    CoachingRelationship createCoachingRelationship(CoachingRelationship coachingRelationship);

    Optional<CoachingRelationship> getCoachingRelationshipById(String id);

    List<CoachingRelationship> getAllCoachingRelationships();

    Optional<List<CoachingRelationship>> getCoachingRelationshipsByCoachId(String coachId);

    Optional<CoachingRelationship> getCoachingRelationshipByAthleteId(String athleteId);

}
