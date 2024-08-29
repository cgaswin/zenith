package org.user.user.service;

import org.user.user.model.CoachingRelationship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachingRelationshipService {
    CoachingRelationship createCoachingRelationship(CoachingRelationship coachingRelationship);

    Optional<CoachingRelationship> getCoachingRelationshipById(UUID id);

    List<CoachingRelationship> getAllCoachingRelationships();

    Optional<List<CoachingRelationship>> getCoachingRelationshipsByCoachId(UUID coachId);

    Optional<CoachingRelationship> getCoachingRelationshipByAthleteId(UUID athleteId);

}
