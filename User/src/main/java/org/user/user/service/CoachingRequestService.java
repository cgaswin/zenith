package org.user.user.service;

import org.user.user.model.CoachingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachingRequestService {
    CoachingRequest createCoachingRequest(CoachingRequest coachingRequest);
    Optional<CoachingRequest> getCoachingRequestById(UUID id);
    List<CoachingRequest> getAllCoachingRequests();
    List<CoachingRequest> getCoachingRequestsByAthleteId(UUID athleteId);
    List<CoachingRequest> getCoachingRequestsByCoachId(UUID coachId);
    void deleteCoachingRequestsByAthleteId(UUID athleteId);
    boolean updateCoachingRequestStatus(UUID id, CoachingRequest.Status status);
    void createCoachingRelationshipForApprovedRequest(UUID coachingRequestId);
}
