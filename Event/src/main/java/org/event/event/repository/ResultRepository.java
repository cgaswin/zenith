package org.event.event.repository;


import org.event.event.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    List<Result> findByEventId(String eventId);
    List<Result> findByEventItemId(String eventItemId);
    List<Result> findByAthleteIdOrderByScoreDesc(String athleteId);
    Optional<Result> findTopByAthleteIdOrderByScoreDesc(String athlete);
}
