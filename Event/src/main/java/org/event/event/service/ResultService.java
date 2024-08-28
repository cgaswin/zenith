package org.event.event.service;

import org.event.event.model.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResultService {
    Result createResult(Result result);
    List<Result> getAllResults();
    Optional<Result> getResultById(UUID id);
    Optional<List<Result>> getResultsByEventId(UUID id);
    Optional<List<Result>> getResultsByEventItemId(UUID id);

}
