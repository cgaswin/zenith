package org.event.event.mappers;

import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
import org.event.event.model.Result;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring",uses = {EventMapper.class, EventItemMapper.class})
public interface ResultMapper {

    ResultResponseDTO resultEntityToResponseDTO(Result result);


    Result resultDtoToResultEntity(ResultRequestDTO resultRequestDTO);

    List<ResultResponseDTO> resultsToResultResponseDTO(List<Result> results);
}

