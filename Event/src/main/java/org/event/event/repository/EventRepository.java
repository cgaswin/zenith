package org.event.event.repository;

import org.event.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;


@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByVenue(String venue);
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByCreatedBy(String createdBy);
}
