package org.user.user.service;

import org.user.user.model.Athlete;
import org.user.user.model.Coach;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachService {
    Coach createCoach(Coach coach);
    Optional<Coach> getCoachById(UUID id);
    List<Coach> getAllCoaches();
    Optional<Coach> updateAcceptingRequests(UUID id, boolean acceptingRequests);
    Optional<Coach> updateCoachDetails(UUID id, Coach coach);

}
