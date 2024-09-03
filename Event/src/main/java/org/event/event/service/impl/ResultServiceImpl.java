package org.event.event.service.impl;

import jakarta.transaction.Transactional;
import org.event.event.dto.ResultRequestDTO;
import org.event.event.dto.ResultResponseDTO;
import org.event.event.exceptions.ResultNotFoundException;
import org.event.event.mappers.ResultMapper;
import org.event.event.model.Result;
import org.event.event.repository.ResultRepository;
import org.event.event.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository, ResultMapper resultMapper) {
        this.resultRepository = resultRepository;
        this.resultMapper = resultMapper;
    }

    @Override
    @Transactional
    public List<Result> createBulkResults(List<ResultRequestDTO> resultRequests) {
        List<Result> results = resultRequests.stream()
                .map(resultMapper::resultRequestDTOToResult)
                .collect(Collectors.toList());
        return resultRepository.saveAll(results);
    }

    @Override
    public List<Result> getResultsByEventId(String eventId) {
        return resultRepository.findByEventId(eventId);
    }

    @Override
    public List<Result> getResultsByEventItemId(String eventItemId) {
        return resultRepository.findByEventItemId(eventItemId);
    }

    @Override
    public List<ResultResponseDTO> getResultsByAthleteId(String athleteId) {
        List<Result> results = resultRepository.findByAthleteIdOrderByScoreDesc(athleteId);
        return resultMapper.resultsToResultResponseDTOs(results); // Convert results to DTOs
    }

    @Override
    public ResultResponseDTO getTopPerformanceByAthleteId(String athleteId) {
        Result topResult = resultRepository.findTopByAthleteIdOrderByScoreDesc(athleteId)
                .orElseThrow(() -> new ResultNotFoundException("No results found for athlete ID: " + athleteId));
        return resultMapper.resultToResultResponseDTO(topResult);
    }

    @Override
    public Result createResult(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public ResultResponseDTO getResultById(String id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with id: " + id));
        return resultMapper.resultToResultResponseDTO(result);
    }
}

