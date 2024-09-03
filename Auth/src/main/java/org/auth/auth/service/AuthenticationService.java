package org.auth.auth.service;

import org.auth.auth.dto.AuthenticationResponseDTO;
import org.auth.auth.exception.AuthenticationException;
import org.auth.auth.exception.InvalidCredentialException;
import org.auth.auth.model.User;
import org.auth.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public User register(User request) {
        logger.info("Registering user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AuthenticationException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthenticationException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = userRepository.save(user);

        logger.info("User {} registered successfully", user.getUsername());
        return user;
    }


    public AuthenticationResponseDTO authenticate(User request) {
        logger.info("Authenticating user with email: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new InvalidCredentialException("Email is required");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.error("Authentication failed: Invalid credentials for email: {}", request.getEmail());
            throw new InvalidCredentialException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        logger.info("User with email {} authenticated successfully", user.getEmail());
        return new AuthenticationResponseDTO(token, user.getId(),user.getUsername(),user.getRole().name(),user.getRoleId());
    }

}
