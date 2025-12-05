package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequestDto;
import com.serjn.online.DTOs.RegisterRequestDto;
import com.serjn.online.sevices.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    void reg(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);
    }

    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody AuthRequestDto authRequest) {
        String token = authService.auth(authRequest);
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(token);

    }

}
