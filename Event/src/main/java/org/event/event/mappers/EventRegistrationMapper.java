package org.event.event.mappers;

import org.event.event.dto.EventRegistrationRequestDTO;
import org.event.event.dto.EventRegistrationResponseDTO;
import org.event.event.model.EventRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {EventMapper.class,EventItemMapper.class})
public interface EventRegistrationMapper {

    @Mapping(source = "eventId", target = "event.id")
    @Mapping(source = "eventItemId", target = "eventItem.id")
    EventRegistration eventRegistrationRequestDtoToEventRegistration(EventRegistrationRequestDTO requestDTO);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "eventItem.id", target = "eventItemId")
    EventRegistrationResponseDTO eventRegistrationToEventRegistrationResponseDTO(EventRegistration eventRegistration);

    List<EventRegistrationResponseDTO> eventRegistrationsToEventRegistrationResponseDTOs(List<EventRegistration> eventRegistrations);
}
