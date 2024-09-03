package org.event.event.repository;

import org.event.event.model.EventItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventItemRepository extends JpaRepository<EventItem, String> {
    @Query("SELECT ei FROM EventItem ei WHERE ei.event.id = :eventId")
    Optional<List<EventItem>> findByEventId(@Param("eventId") String eventId);
}
