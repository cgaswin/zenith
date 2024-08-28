package org.event.event.service.impl;

import org.event.event.model.EventRegistration;
import org.event.event.repository.EventRegistrationRepository;
import org.event.event.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
    public Optional<EventRegistration> getEventRegistrationById(UUID id) {
        return eventRegistrationRepository.findById(id);
    }

    @Override
    public Optional<List<EventRegistration>> getRegistrationsByEventId(UUID eventId) {
        return eventRegistrationRepository.findByEventId(eventId);
    }

    @Override
    public Optional<List<EventRegistration>> getRegistrationsByEventItemId(UUID eventItemId) {
        return eventRegistrationRepository.findByEventItemId(eventItemId);
    }

    @Override
    public Optional<EventRegistration> updateRegistrationStatus(UUID registrationId, EventRegistration.Status status) {
        return eventRegistrationRepository.findById(registrationId)
                .map(registration -> {
                    registration.setStatus(status);
                    return eventRegistrationRepository.save(registration);
                });
    }


}
