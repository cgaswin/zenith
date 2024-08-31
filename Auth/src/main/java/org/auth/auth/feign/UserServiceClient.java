package org.auth.auth.feign;

import org.auth.auth.client.AthleteRequestDTO;
import org.auth.auth.client.AthleteResponseDTO;
import org.auth.auth.client.CoachRequestDTO;
import org.auth.auth.client.CoachResponseDTO;
import org.auth.auth.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {
    @PostMapping("/api/v1/athlete")
    ResponseEntity<ResponseDTO<AthleteResponseDTO>> createAthlete(
            @RequestBody AthleteRequestDTO athleteRequest,
            @RequestHeader("Authorization") String authToken
    );

    @PostMapping("/api/v1/coach")
    ResponseEntity<ResponseDTO<CoachResponseDTO>> createCoach(
            @RequestBody CoachRequestDTO coachRequest,
            @RequestHeader("Authorization") String authToken
    );
}
