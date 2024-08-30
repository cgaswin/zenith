package org.auth.auth.controller;

import org.auth.auth.dto.AuthenticationResponseDTO;
import org.auth.auth.dto.RegistrationResponseDTO;
import org.auth.auth.dto.ResponseDTO;
import org.auth.auth.model.User;
import org.auth.auth.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> register(@RequestBody User user) {
        logger.info("Received request to register user: {}", user);
        User registeredUser = authenticationService.register(user);

        RegistrationResponseDTO registrationResponseDTO = new RegistrationResponseDTO(
                registeredUser.getId(),
                registeredUser.getName(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getRole()
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
}



