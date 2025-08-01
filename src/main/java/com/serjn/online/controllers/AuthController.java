package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.sevices.utils.AuthService;
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
    ResponseEntity<String> reg(@RequestBody RegRequest regRequest) {
        try {
            authService.register(regRequest);
        } catch (AuthFailedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Register failed");
        }
        return null;
    }


    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody AuthRequest authRequest) {
        try {
            String token = (authService.auth(authRequest));
            return ResponseEntity.status(200).body(token);
        } catch (AuthFailedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Auth failed");
        }
    }

}
