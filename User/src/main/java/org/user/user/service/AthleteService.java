package org.user.user.service;

import org.user.user.dto.StatDTO;
import org.user.user.model.Athlete;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AthleteService {
    Athlete createAthlete(Athlete athlete);
    Optional<Athlete> getAthleteById(String id);
    Athlete updateAthlete(String id, Athlete athlete);
    List<Athlete> getAllAthletes();
    StatDTO getStats();
}
