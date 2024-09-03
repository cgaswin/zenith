package org.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.user.user.model.CoachingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoachingRequestRepository extends JpaRepository<CoachingRequest, String> {
    List<CoachingRequest> findByAthleteId(String athleteId);

    List<CoachingRequest> findByCoachId(String coachId);

    @Query("SELECT cr FROM CoachingRequest cr WHERE cr.athlete.id = :athleteId ORDER BY cr.requestDate DESC")
    Optional<CoachingRequest> findLatestByAthleteId(@Param("athleteId") String athleteId);
}
