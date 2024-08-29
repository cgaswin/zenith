package org.user.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.CoachingRelationship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoachingRelationshipRepository extends JpaRepository<CoachingRelationship, UUID> {
    Optional<List<CoachingRelationship>> findByCoachId(UUID coachId);
    Optional<CoachingRelationship> findByAthleteId(UUID athleteId);
}
