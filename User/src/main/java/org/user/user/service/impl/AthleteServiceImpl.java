package org.user.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.user.model.Athlete;
import org.user.user.repository.AthleteRepository;
import org.user.user.service.AthleteService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    @Autowired
    public AthleteServiceImpl(AthleteRepository athleteRepository){
        this.athleteRepository = athleteRepository;
    }

    @Override
    public Athlete createAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    @Override
    public Optional<Athlete> getAthleteById(UUID id) {
        return athleteRepository.findById(id);
    }

    @Override
    public Athlete updateAthlete(UUID id, Athlete athlete) {
        return athleteRepository.findById(id).map(existingAthlete -> {
            if (athlete.getDescription() != null) {
                existingAthlete.setDescription(athlete.getDescription());
            }
            if (athlete.getPhotoUrl() != null) {
                existingAthlete.setPhotoUrl(athlete.getPhotoUrl());
            }
            return athleteRepository.save(existingAthlete);
        }).orElse(null);
    }

    @Override
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }
}
