package org.event.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRegistration extends JpaRepository<EventRegistration, UUID> {
}
