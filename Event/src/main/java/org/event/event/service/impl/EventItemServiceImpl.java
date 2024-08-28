package org.event.event.service.impl;

import org.event.event.model.EventItem;
import org.event.event.repository.EventItemRepository;
import org.event.event.service.EventItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventItemServiceImpl implements EventItemService {

    private final EventItemRepository eventItemRepository;

    public EventItemServiceImpl(EventItemRepository eventItemRepository){
        this.eventItemRepository  = eventItemRepository;
    }

    @Override
    public EventItem createEventItem(EventItem eventItem) {
        return eventItemRepository.save((eventItem));
    }

    @Override
    public List<EventItem> getAllEventItems() {
        return eventItemRepository.findAll();
    }

    @Override
    public Optional<EventItem> getEventItemById(UUID id) {
        return eventItemRepository.findById(id);
    }

    @Override
    public Optional<List<EventItem>> getEventItemsByEventId(UUID eventId) {
       return eventItemRepository.findByEventId(eventId);
    }


}
