package org.user.user.service;

import org.user.user.model.CoachingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachingRequestService {
    CoachingRequest createCoachingRequest(CoachingRequest coachingRequest);
    Optional<CoachingRequest> getCoachingRequestById(String id);
    List<CoachingRequest> getAllCoachingRequests();
    List<CoachingRequest> getCoachingRequestsByAthleteId(String athleteId);
    List<CoachingRequest> getCoachingRequestsByCoachId(String coachId);
    void deleteCoachingRequestsByAthleteId(String athleteId);
    boolean updateCoachingRequestStatus(String id, CoachingRequest.Status status);
    void createCoachingRelationshipForApprovedRequest(String coachingRequestId);
}
