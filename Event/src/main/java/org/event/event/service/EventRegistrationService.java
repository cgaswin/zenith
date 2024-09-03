package org.event.event.service;

import org.event.event.model.EventRegistration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRegistrationService {

    EventRegistration createEventRegistration(EventRegistration eventRegistration);

    List<EventRegistration> getAllEventRegistrations();

    Optional<EventRegistration> getEventRegistrationById(String id);

    Optional<List<EventRegistration>> getRegistrationsByEventId(String eventId);

    Optional<List<EventRegistration>> getRegistrationsByEventItemId(String eventItemId);

    Optional<List<EventRegistration>> getRegistrationsByAthleteId(String athleteId);

    boolean isAthleteRegisteredForEventItem(String athleteId, String eventItemId);

    Optional<List<EventRegistration>> getRegistrationsByEventAndAthlete(String eventId, String athleteId);

    List<EventRegistration> getPendingRegistrations();
}
