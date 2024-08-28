package org.event.event.service;

import org.event.event.model.EventRegistration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRegistrationService {

    EventRegistration createEventRegistration(EventRegistration eventRegistration);

    List<EventRegistration> getAllEventRegistrations();

    Optional<EventRegistration> getEventRegistrationById(UUID id);

    Optional<List<EventRegistration>> getRegistrationsByEventId(UUID eventId);

    Optional<List<EventRegistration>> getRegistrationsByEventItemId(UUID eventItemId);

    Optional<EventRegistration> updateRegistrationStatus(UUID registrationId, EventRegistration.Status status);
}
