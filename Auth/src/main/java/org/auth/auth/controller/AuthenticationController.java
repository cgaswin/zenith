package org.auth.auth.controller;

import org.auth.auth.client.AthleteRequestDTO;
import org.auth.auth.client.AthleteResponseDTO;
import org.auth.auth.client.CoachRequestDTO;
import org.auth.auth.client.CoachResponseDTO;
import org.auth.auth.dto.AuthenticationResponseDTO;
import org.auth.auth.dto.RegistrationResponseDTO;
import org.auth.auth.dto.ResponseDTO;
import org.auth.auth.dto.UserRegistrationDTO;
import org.auth.auth.feign.UserServiceClient;
import org.auth.auth.model.Role;
import org.auth.auth.model.User;
import org.auth.auth.repository.UserRepository;
import org.auth.auth.service.AuthenticationService;
import org.auth.auth.service.JwtService;
import org.auth.auth.service.S3FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    private final S3FileUpload s3FileUpload;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService, UserServiceClient userServiceClient, S3FileUpload s3FileUpload, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userServiceClient=userServiceClient;
        this.s3FileUpload = s3FileUpload;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/register",consumes = {"multipart/form-data" })
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> register(@ModelAttribute UserRegistrationDTO userRegistrationDTO) throws IOException {
        logger.info("Received request to register user: {}", userRegistrationDTO);

        User user = new User();
        user.setName(userRegistrationDTO.getName());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setRole(Role.valueOf(userRegistrationDTO.getRole().toUpperCase()));

        String photoUrl = s3FileUpload.uploadFile(userRegistrationDTO.getImage());
        User registeredUser = authenticationService.register(user);
        userRegistrationDTO.setPhotoUrl(photoUrl);


        if (registeredUser.getRole() == Role.ATHLETE) {
            String roleId = createAthleteInUserService(registeredUser, userRegistrationDTO);
            System.out.print(roleId);
            registeredUser.setRoleId(roleId);
            userRepository.save(registeredUser);
        } else if (registeredUser.getRole() == Role.COACH) {
            String roleId = createCoachInUserService(registeredUser, userRegistrationDTO);
            System.out.print(roleId);
            registeredUser.setRoleId(roleId);
            userRepository.save(registeredUser);
        }

        RegistrationResponseDTO registrationResponseDTO = new RegistrationResponseDTO(
                registeredUser.getId(),
                registeredUser.getName(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getRole(),
                registeredUser.getRoleId()
        );
        ResponseDTO<RegistrationResponseDTO> response = new ResponseDTO<>("User registered successfully", true, registrationResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<AuthenticationResponseDTO>> login(@RequestBody User request) {
        logger.info("Request to log in with {}", request.getEmail() != null ? "email: " + request.getEmail() : "username: " + request.getUsername());
        AuthenticationResponseDTO authResponseDTO = authenticationService.authenticate(request);
        ResponseDTO<AuthenticationResponseDTO> response = new ResponseDTO<>("Login successful", true, authResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getSecureData() {
        //This is a dummy route to check if authentication works
        return ResponseEntity.ok("This is secure data only accessible to authenticated users!");
    }

    private String createAthleteInUserService(User user, UserRegistrationDTO registrationDTO) {
        AthleteRequestDTO athleteRequest = new AthleteRequestDTO();
        athleteRequest.setName(user.getName());
        athleteRequest.setDob(registrationDTO.getDob());
        athleteRequest.setGender(registrationDTO.getGender());
        athleteRequest.setHeight(registrationDTO.getHeight());
        athleteRequest.setWeight(registrationDTO.getWeight());
        athleteRequest.setCategory(registrationDTO.getCategory());
        athleteRequest.setDescription(registrationDTO.getDescription());
        athleteRequest.setPhotoUrl(registrationDTO.getPhotoUrl());

        String authToken = "Bearer " + jwtService.generateToken(user);

        try {
            ResponseEntity<ResponseDTO<AthleteResponseDTO>> response =  userServiceClient.createAthlete(athleteRequest, authToken);
            logger.info("Athlete created in user service for user: {}", user.getId());
            return  Objects.requireNonNull(response.getBody()).getData().getId();
        } catch (Exception e) {
            logger.error("Failed to create athlete in user service: {}", e.getMessage());
        }
        return "";
    }

    private String createCoachInUserService(User user, UserRegistrationDTO registrationDTO) {
        CoachRequestDTO coachRequest = new CoachRequestDTO();
        coachRequest.setName(user.getName());
        coachRequest.setDob(registrationDTO.getDob());
        coachRequest.setGender(registrationDTO.getGender());
        coachRequest.setCategory(registrationDTO.getCategory());
        coachRequest.setDescription(registrationDTO.getDescription());
        coachRequest.setPhotoUrl(registrationDTO.getPhotoUrl());
        coachRequest.setAchievements(registrationDTO.getAchievements());

        String authToken = "Bearer " + jwtService.generateToken(user);

        try {
            ResponseEntity<ResponseDTO<CoachResponseDTO>> response = userServiceClient.createCoach(coachRequest, authToken);
            System.out.println(response);
            logger.info("Coach created in user service for user: {}", user.getId());
            return  Objects.requireNonNull(response.getBody()).getData().getId();
        } catch (Exception e) {
            logger.error("Failed to create coach in user service: {}", e.getMessage());
        }
        return "";
    }
}



