package org.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.CoachingRequest;

import java.util.UUID;

@Repository
public interface CoachingRequestRepository extends JpaRepository<CoachingRequest, UUID> {
}
