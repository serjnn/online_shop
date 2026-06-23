package com.serjn.online.services;


import com.serjn.online.model.dto.AuthRequestDto;
import com.serjn.online.model.dto.RegisterRequestDto;
import com.serjn.online.jwt.JwtService;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.model.entities.Bucket;
import com.serjn.online.model.entities.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final ClientRepository clientRepository;

    @Transactional
    public void register(RegisterRequestDto registerRequestDto) {
        if (clientRepository.findByMail(registerRequestDto.mail()).isPresent()) {
            throw new IllegalArgumentException("Client with this email already exists");
        }

        Bucket bucket = new Bucket();

        Client client = new Client(
                registerRequestDto.mail(),
                passwordEncoder.encode(registerRequestDto.password()),
                bucket,
                "client"
        );
        bucket.setClient(client);
        client.setBalance(BigDecimal.valueOf(500));
        clientRepository.save(client);

    }

    public String auth(AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.mail(),
                    authRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new AuthFailedException("Incorrect mail or password");
        }

        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.mail());
        String token = jwtService.generateToken(userDetails);
        Assert.hasText(token,"Token is null");
        return token;
    }



}
