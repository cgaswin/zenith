package org.event.event.service;

import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
import org.event.event.model.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResultService {

    List<Result> createBulkResults(List<ResultRequestDTO> resultRequests);
    List<Result> getResultsByEventId(String eventId);
    List<Result> getResultsByEventItemId(String eventItemId);
    List<ResultResponseDTO> getResultsByAthleteId(String athleteId); // Ensure this matches the implementation
    ResultResponseDTO getTopPerformanceByAthleteId(String athleteId);
    Result createResult(Result result);
    List<Result> getAllResults();
    ResultResponseDTO getResultById(String id);
}
