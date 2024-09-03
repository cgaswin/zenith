package org.user.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.user.dto.CoachingRequestResponseDTO;
import org.user.user.dto.StatDTO;
import org.user.user.exception.AthleteNotFoundException;
import org.user.user.mapper.CoachingRequestMapper;
import org.user.user.mapper.CoachingRequestMapperImpl;
import org.user.user.model.Athlete;
import org.user.user.model.CoachingRelationship;
import org.user.user.model.CoachingRequest;
import org.user.user.repository.AthleteRepository;
import org.user.user.repository.CoachingRelationshipRepository;
import org.user.user.repository.CoachingRequestRepository;
import org.user.user.service.AthleteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;
    private final CoachingRelationshipRepository coachingRelationshipRepository;
    private final CoachingRequestRepository coachingRequestRepository;
    private final CoachingRequestMapper coachingRequestMapper;

    @Autowired
    public AthleteServiceImpl(AthleteRepository athleteRepository, CoachingRelationshipRepository coachingRelationshipRepository, CoachingRequestRepository coachingRequestRepository, CoachingRequestMapper coachingRequestMapper){
        this.athleteRepository = athleteRepository;
        this.coachingRelationshipRepository = coachingRelationshipRepository;
        this.coachingRequestRepository = coachingRequestRepository;
        this.coachingRequestMapper = coachingRequestMapper;
    }

    @Override
    public Athlete createAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    @Override
    public Optional<Athlete> getAthleteById(String id) {
        return athleteRepository.findById(id);
    }



    @Override
    public Athlete updateAthlete(String id, Athlete athlete) {
        return athleteRepository.findById(id).map(existingAthlete -> {
            if (athlete.getName() != null) {
                existingAthlete.setName(athlete.getName());
            }
            if (athlete.getDob() != null) {
                existingAthlete.setDob(athlete.getDob());
            }
            if (athlete.getGender() != null) {
                existingAthlete.setGender(athlete.getGender());
            }
            if (athlete.getHeight() != null) {
                existingAthlete.setHeight(athlete.getHeight());
            }
            if (athlete.getWeight() != null) {
                existingAthlete.setWeight(athlete.getWeight());
            }
            if (athlete.getCategory() != null) {
                existingAthlete.setCategory(athlete.getCategory());
            }
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

    @Override
    public StatDTO getStats() {
        List<Athlete> athletes = athleteRepository.findAll();
        Map<String, Integer> dailyAthleteCounts = new HashMap<>();
        long totalValidAthletes = 0;

        for (Athlete athlete : athletes) {
            LocalDateTime createdAt = athlete.getCreatedAt();
            if (createdAt != null) {
                LocalDate athleteDate = createdAt.toLocalDate();
                String dateKey = athleteDate.toString();
                dailyAthleteCounts.put(dateKey, dailyAthleteCounts.getOrDefault(dateKey, 0) + 1);
                totalValidAthletes++;
            } else {
                log.warn("Athlete with ID {} has null createdAt value", athlete.getId());
            }
        }

        StatDTO statDTO = new StatDTO();
        statDTO.setTotal(totalValidAthletes);
        statDTO.setDailyCounts(dailyAthleteCounts);

        log.info("Generated stats: total athletes = {}, daily counts = {}", totalValidAthletes, dailyAthleteCounts);

        return statDTO;
    }

    public CoachingRequestResponseDTO getCoachingStatus(String athleteId) {
        Optional<CoachingRelationship> relationship = coachingRelationshipRepository.findByAthleteId(athleteId);
        Optional<CoachingRequest> request = coachingRequestRepository.findLatestByAthleteId(athleteId);

        if (relationship.isPresent()) {
            CoachingRelationship rel = relationship.get();
            CoachingRequest approvedRequest = new CoachingRequest();
            approvedRequest.setId(rel.getId());
            approvedRequest.setAthlete(rel.getAthlete());
            approvedRequest.setCoach(rel.getCoach());
            approvedRequest.setStatus(CoachingRequest.Status.APPROVED);
            approvedRequest.setRequestDate(rel.getStartDate());
            return coachingRequestMapper.coachingRequestToCoachingRequestResponseDto(approvedRequest);
        } else if (request.isPresent()) {
            return coachingRequestMapper.coachingRequestToCoachingRequestResponseDto(request.get());
        } else {
            return null;
        }
    }



}

