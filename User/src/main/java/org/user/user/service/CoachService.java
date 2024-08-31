package org.user.user.service;

import org.user.user.model.Athlete;
import org.user.user.model.Coach;

import java.util.List;
import java.util.Optional;


public interface CoachService {
    Coach createCoach(Coach coach);
    Optional<Coach> getCoachById(String id);
    List<Coach> getAllCoaches();
    Optional<Coach> updateAcceptingRequests(String id, boolean acceptingRequests);
    Optional<Coach> updateCoachDetails(String id, Coach coach);

}
