package com.serjn.online.sevices;


import com.serjn.online.DTOs.AuthRequestDto;
import com.serjn.online.DTOs.RegisterRequestDto;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.model.Bucket;
import com.serjn.online.model.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final ClientRepository clientRepository;

    public void register(RegisterRequestDto registerRequestDto) {

        Bucket bucket = new Bucket();

        Client client = new Client(
                registerRequestDto.getMail(),
                passwordEncoder.encode(registerRequestDto.getPassword()),
                bucket,
                "client"
        );
        bucket.setClient(client);
        client.setBalance(BigDecimal.valueOf(500));
        clientRepository.save(client);

    }

    public String auth(@RequestBody AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getMail(),
                    authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AuthFailedException("Incorrect mail or password");
        }

        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.getMail());
        String token = jwtService.generateToken(userDetails);
        Assert.hasText(token,"Token is null");
        return token;
    }



}
