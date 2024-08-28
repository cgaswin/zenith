package org.event.event.repository;


import org.event.event.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, UUID> {
    Optional<List<Result>> findByEventId(UUID eventId);
    Optional<List<Result>> findByEventItemId(UUID eventItemId);
}
