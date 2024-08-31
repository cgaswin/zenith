package org.user.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.user.user.dto.CoachRequestDTO;
import org.user.user.dto.CoachResponseDTO;
import org.user.user.model.Coach;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachMapper {
    @Mapping(source = "userId", target = "userId")
    Coach coachRequestDtoToCoach(CoachRequestDTO coachRequestDTO);
    CoachResponseDTO coachToCoachResponseDto(Coach coach);
    List<CoachResponseDTO> coachesToCoachResponseDto(List<Coach> coaches);
}
