package org.user.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.user.user.dto.CoachingRequestRequestDTO;
import org.user.user.dto.CoachingRequestResponseDTO;
import org.user.user.model.CoachingRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachingRequestMapper {

    @Mapping(source = "athleteId", target = "athlete.id")
    @Mapping(source = "coachId", target = "coach.id")
    CoachingRequest coachingRequestRequestDtoToCoachingRequest(CoachingRequestRequestDTO coachingRequestRequestDTO);

    @Mapping(source = "athlete", target = "athlete")
    @Mapping(source = "coach", target = "coach")
    CoachingRequestResponseDTO coachingRequestToCoachingRequestResponseDto(CoachingRequest coachingRequest);

    List<CoachingRequestResponseDTO> coachingRequestListToCoachingRequestResponseDtoList(List<CoachingRequest> coachingRequestList);


}
