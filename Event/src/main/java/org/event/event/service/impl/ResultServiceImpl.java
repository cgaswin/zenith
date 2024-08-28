package org.event.event.service.impl;

import org.event.event.model.Result;
import org.event.event.repository.ResultRepository;
import org.event.event.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository){
        this.resultRepository = resultRepository;
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
    public Optional<Result> getResultById(UUID id) {
        return resultRepository.findById(id);
    }

    @Override
    public Optional<List<Result>> getResultsByEventId(UUID id) {
        return resultRepository.findByEventId(id);
    }

    @Override
    public Optional<List<Result>> getResultsByEventItemId(UUID id) {
        return resultRepository.findByEventItemId(id);
    }
}
