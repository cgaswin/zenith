package org.event.event.service;

import org.event.event.model.EventItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventItemService {
    EventItem createEventItem(EventItem eventItem);

    List<EventItem> getAllEventItems();

    Optional<EventItem> getEventItemById(String id);

    Optional<List<EventItem>> getEventItemsByEventId(String id);


}
