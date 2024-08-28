package org.event.event.mappers;

import org.event.event.dto.EventItemRequestDTO;
import org.event.event.dto.EventItemResponseDTO;
import org.event.event.model.EventItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = EventMapper.class)
public interface EventItemMapper {


    EventItem eventItemRequestDtoToEventItem(EventItemRequestDTO eventItemRequestDTO);

    @Mapping(source = "event.id", target = "eventId")
    EventItemResponseDTO eventItemToEventItemResponseDTO(EventItem eventItem);
}
