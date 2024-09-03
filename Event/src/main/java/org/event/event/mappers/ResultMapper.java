package org.event.event.mappers;

import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
import org.event.event.model.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring",uses = {EventMapper.class, EventItemMapper.class})
public interface ResultMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event.id", source = "eventId")
    @Mapping(target = "eventItem.id", source = "eventItemId")
    Result resultRequestDTOToResult(ResultRequestDTO resultRequestDTO);

    @Mapping(target = "eventName", source = "event.name")
    @Mapping(target = "eventItemName", source = "eventItem.name")
    ResultResponseDTO resultToResultResponseDTO(Result result);

    List<ResultResponseDTO> resultsToResultResponseDTOs(List<Result> results);
}

