package org.event.event.repository;

import org.event.event.model.EventItem;
import org.event.event.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {
    Optional<List<EventRegistration>> findByEventId(String eventId);
    Optional<List<EventRegistration>> findByEventItemId(String eventId);

    boolean existsByAthleteIdAndEventItemId(String athleteId, String eventItemId);

    List<EventRegistration> findByEventIdAndAthleteId(String eventId, String athleteId);

    List<EventRegistration> findByStatus(EventRegistration.Status status);

    List<EventRegistration> findByEventItemIdAndStatus(String eventItemId, EventRegistration.Status status);

    List<EventRegistration> findByAthleteId(String athleteId);
}
