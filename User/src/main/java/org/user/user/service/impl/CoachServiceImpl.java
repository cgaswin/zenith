package org.user.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.user.model.Coach;
import org.user.user.repository.CoachRepository;
import org.user.user.service.CoachService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachServiceImpl implements CoachService {

    private final CoachRepository coachRepository;

    @Autowired
    public CoachServiceImpl(CoachRepository  coachRepository){
        this.coachRepository = coachRepository;
    }

    @Override
    public Coach createCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    @Override
    public Optional<Coach> getCoachById(String id) {
        return coachRepository.findById(id);
    }

    @Override
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    @Override
    public Optional<Coach> updateAcceptingRequests(String id, boolean acceptingRequests) {
        return coachRepository.findById(id).map(coach -> {
            coach.setAcceptingRequests(acceptingRequests); // Ensure you have a setter for this field
            return coachRepository.save(coach);
        });
    }

    @Override
    public Optional<Coach> updateCoachDetails(String id, Coach updatedCoach) {
        return coachRepository.findById(id).map(coach -> {
            if (updatedCoach.getName() != null) {
                coach.setName(updatedCoach.getName());
            }
            if (updatedCoach.getGender() != null) {
                coach.setGender(updatedCoach.getGender());
            }
            if (updatedCoach.getDob() != null) {
                coach.setDob(updatedCoach.getDob());
            }
            if (updatedCoach.getCategory() != null) {
                coach.setCategory(updatedCoach.getCategory());
            }
            if (updatedCoach.getDescription() != null) {
                coach.setDescription(updatedCoach.getDescription());
            }
            if (updatedCoach.getPhotoUrl() != null) {
                coach.setPhotoUrl(updatedCoach.getPhotoUrl());
            }
            coach.setAcceptingRequests(updatedCoach.isAcceptingRequests());
            if (updatedCoach.getAchievements() != null) {
                coach.setAchievements(updatedCoach.getAchievements());
            }

            return coachRepository.save(coach);
        });
    }
}
