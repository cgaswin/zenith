package org.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.Coach;

import java.util.UUID;

@Repository
public interface CoachRepository extends JpaRepository<Coach, UUID> {
}
