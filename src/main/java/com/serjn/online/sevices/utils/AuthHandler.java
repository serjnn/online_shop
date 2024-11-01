package com.serjn.online.sevices.utils;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientDetailService;
import com.serjn.online.sevices.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthHandler {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final ClientService clientService;


    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getMail(),
                    authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.getMail());

        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<HttpStatus> register(RegRequest regRequest) {
        if (regRequest.getMail() == null || regRequest.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Bucket bucket = new Bucket();

        Client client = new Client(
                regRequest.getMail(),
                passwordEncoder.encode(regRequest.getPassword()),
                bucket,
                regRequest.getRole().toLowerCase()
        );
        bucket.setClient(client);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
