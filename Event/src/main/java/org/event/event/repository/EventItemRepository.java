package org.event.event.repository;

import org.event.event.model.EventItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventItemRepository extends JpaRepository<EventItem, UUID> {
    Optional<List<EventItem>> findByEventId(UUID eventId);
}
