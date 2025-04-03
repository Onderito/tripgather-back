package org.wcs.tripgather.controller;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.wcs.tripgather.exception.EmailAlreadyInUseException;
import org.wcs.tripgather.model.User;
import org.wcs.tripgather.service.JwtService;
import org.wcs.tripgather.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Attempting to register user: {}", user.getEmail());

        // Normalize email
        String normalizedEmail = user.getEmail().toLowerCase();
        user.setEmail(normalizedEmail);

        try {
            // Validate input data
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                logger.warn("Password is missing for user: {}", user.getEmail());
                return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
            }

            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                logger.warn("Email is missing for user registration");
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            // Register the user
            User savedUser = userService.register(
                    user.getEmail(),
                    user.getPassword(),
                    user.getBio(),
                    user.getCountry(),
                    user.getFirstName(),
                    user.getGender(),
                    user.getImageUrl(),
                    user.getLastName(),
                    user.getBirthDate()
            );

            logger.info("User registered successfully: {}", savedUser.getEmail());

            // Return a successful response with the saved user's ID
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedUser.getId());
            response.put("message", "User registered successfully");

            return ResponseEntity.status(201).body(response);
        } catch (EmailAlreadyInUseException e) {
            logger.warn("Email already in use: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email is already in use"));
        } catch (Exception e) {
            logger.error("Error during registration for user {}: {}", user.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        try {
            User foundUser = userService.findByEmail(user.getEmail());
            if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                String token = jwtService.generateToken(foundUser);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", Map.of(
                        "id", foundUser.getId(),
                        "firstName", foundUser.getFirstName(),
                        "lastName", foundUser.getLastName(),
                        "email", foundUser.getEmail(),
                        "imageUrl", foundUser.getImageUrl(),
                        "gender", foundUser.getGender().toString(), // Convertir Enum en String
                        "bio", foundUser.getBio(),
                        "country", foundUser.getCountry()
                ));

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid email or password");
                return ResponseEntity.status(401).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error during login: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
//        try {
//            User foundUser = userService.findByEmail(user.getEmail());
//            if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
//                String token = jwtService.generateToken(foundUser);
//                Map<String, String> response = new HashMap<>();
//                response.put("token", token);
//                return ResponseEntity.ok(response);
//            } else {
//
//                Map<String, String> errorResponse = new HashMap<>();
//                errorResponse.put("message", "Invalid email or password");
//                return ResponseEntity.status(401).body(errorResponse);
//            }
//        } catch (Exception e) {
//
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Error during login: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
}