package org.user.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.CoachingRelationship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoachingRelationshipRepository extends JpaRepository<CoachingRelationship, String> {
    Optional<List<CoachingRelationship>> findByCoachId(String coachId);
    Optional<CoachingRelationship> findByAthleteId(String athleteId);
}
