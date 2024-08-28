package org.event.event.repository;

import org.event.event.model.EventItem;
import org.event.event.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    Optional<List<EventRegistration>> findByEventId(UUID eventId);
    Optional<List<EventRegistration>> findByEventItemId(UUID eventId);
}
