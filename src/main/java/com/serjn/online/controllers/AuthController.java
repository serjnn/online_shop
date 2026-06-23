package com.serjn.online.controllers;


import com.serjn.online.model.dto.AuthRequestDto;
import com.serjn.online.model.dto.RegisterRequestDto;
import com.serjn.online.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @PostMapping("/register")
    void reg(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token")
    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody AuthRequestDto authRequest) {
        String token = authService.auth(authRequest);
        return ResponseEntity.ok(token);

    }

}
