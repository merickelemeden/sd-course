package com.sd_project.sd_course.controller;

import com.sd_project.sd_course.dto.request.LoginRequest;
import com.sd_project.sd_course.dto.request.RegisterRequest;
import com.sd_project.sd_course.dto.response.JwtResponse;
import com.sd_project.sd_course.dto.response.MessageResponse;
import com.sd_project.sd_course.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with username, email, and password")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        MessageResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Refresh access token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam String refreshToken) {
        JwtResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
} 