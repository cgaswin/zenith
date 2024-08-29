package org.user.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.user.user.dto.CoachingRelationshipRequestDTO;
import org.user.user.dto.CoachingRelationshipResponseDTO;
import org.user.user.model.CoachingRelationship;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachingRelationshipMapper {
    @Mapping(source = "athleteId", target = "athlete.id")
    @Mapping(source = "coachId", target = "coach.id")
    CoachingRelationship coachingRelationshipRequestDtoToCoachingRelationship(CoachingRelationshipRequestDTO coachingRelationshipRequestDTO);

    @Mapping(source = "athlete", target = "athlete")
    @Mapping(source = "coach", target = "coach")
    CoachingRelationshipResponseDTO coachingRelationshipToCoachingRelationshipResponseDto(CoachingRelationship coachingRelationship);

    List<CoachingRelationshipResponseDTO> coachingRelationshipListToCoachingRelationshipResponseDtoList(List<CoachingRelationship> coachingRelationshipList);
}
