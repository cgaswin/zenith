package org.event.event.service;

import org.event.event.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    Event createEvent(Event event);

    List<Event> getAllEvents();

    Optional<Event> getEventById(UUID id);

    List<Event> getEventsByVenue(String venue);

    List<Event> getEventsByName(String name);

    List<Event> getEventsCreatedBy(UUID createdBy);

    List<Event> listUpcomingEvents();

    List<Event> listPastEvents();
}
