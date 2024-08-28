package org.event.event.mappers;

import org.event.event.dto.EventResponseDTO;
import org.event.event.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponseDTO eventToEventResponseDTO(Event event);


}
