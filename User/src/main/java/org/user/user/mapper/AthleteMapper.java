package org.user.user.mapper;

import org.mapstruct.Mapper;
import org.user.user.dto.AthleteRequestDTO;
import org.user.user.dto.AthleteResponseDTO;
import org.user.user.model.Athlete;

@Mapper(componentModel = "spring")
public interface AthleteMapper {
    Athlete athleteRequestDtoToAthlete(AthleteRequestDTO athleteRequestDTO);

    AthleteResponseDTO athleteToAthleteResponseDTO(Athlete athlete);
}
