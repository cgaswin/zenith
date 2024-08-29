package org.user.user.service;

import org.user.user.model.Athlete;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AthleteService {
    Athlete createAthlete(Athlete athlete);
    Optional<Athlete> getAthleteById(UUID id);
    Athlete updateAthlete(UUID id, Athlete athlete);

    List<Athlete> getAllAthletes();
}
