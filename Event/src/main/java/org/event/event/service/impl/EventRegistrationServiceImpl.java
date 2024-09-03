package org.event.event.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.event.event.exceptions.EventRegistrationNotFoundException;
import org.event.event.model.EventRegistration;
import org.event.event.repository.EventRegistrationRepository;
import org.event.event.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    public EventRegistrationServiceImpl(EventRegistrationRepository eventRegistrationRepository){
        this.eventRegistrationRepository = eventRegistrationRepository;
    }


    @Override
    public EventRegistration createEventRegistration(EventRegistration eventRegistration) {
        return eventRegistrationRepository.save(eventRegistration);
    }

    @Override
    public List<EventRegistration> getAllEventRegistrations() {
        return eventRegistrationRepository.findAll();
    }

    @Override
    public Optional<EventRegistration> getEventRegistrationById(String id) {
        return eventRegistrationRepository.findById(id);
    }

    @Override
    public Optional<List<EventRegistration>> getRegistrationsByEventId(String eventId) {
        return eventRegistrationRepository.findByEventId(eventId);
    }

    @Override
    public Optional<List<EventRegistration>> getRegistrationsByEventItemId(String eventItemId) {
        return eventRegistrationRepository.findByEventItemId(eventItemId);
    }

    @Override
    public Optional<List<EventRegistration>> getRegistrationsByAthleteId(String athleteId) {
        List<EventRegistration> registrations = eventRegistrationRepository.findByAthleteId(athleteId);
        return registrations.isEmpty() ? Optional.empty() : Optional.of(registrations);
    }


    @Transactional
    public EventRegistration updateRegistrationStatus(String registrationId, EventRegistration.Status newStatus) {
        EventRegistration registration = eventRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new EventRegistrationNotFoundException("Event registration not found with id: " + registrationId));

        registration.setStatus(newStatus);
        return eventRegistrationRepository.save(registration);
    }
    public Optional<List<EventRegistration>> getRegistrationsByEventAndAthlete(String eventId, String athleteId) {
        log.info("Fetching registrations for event ID: {} and athlete ID: {}", eventId, athleteId);
        List<EventRegistration> registrations = eventRegistrationRepository.findByEventIdAndAthleteId(eventId, athleteId);
        log.info("Found {} registrations", registrations.size());
        return Optional.of(registrations);
    }

    public boolean isAthleteRegisteredForEventItem(String athleteId, String eventItemId) {
        return eventRegistrationRepository.existsByAthleteIdAndEventItemId(athleteId, eventItemId);
    }



    @Override
    public List<EventRegistration> getPendingRegistrations() {
        return eventRegistrationRepository.findByStatus(EventRegistration.Status.PENDING);

    }

    public List<EventRegistration> getApprovedRegistrationsByEventItemId(String eventItemId) {
        return eventRegistrationRepository.findByEventItemIdAndStatus(eventItemId, EventRegistration.Status.APPROVED);
    }


}
