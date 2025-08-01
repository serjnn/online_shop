package com.serjn.online.sevices.utils;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientDetailService;
import com.serjn.online.sevices.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final ClientService clientService;


    public String auth(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getMail(),
                    authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {

            throw new AuthFailedException();
        }
        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.getMail());

        return jwtService.generateToken(userDetails);
    }

    public void register(RegRequest regRequest) {
        if (regRequest.getMail() == null || regRequest.getPassword() == null || !regRequest.getPassword().equals(
                regRequest.getRepeatPassword()
        )) {
            throw new AuthFailedException();
        }

        Bucket bucket = new Bucket();

        Client client = new Client(
                regRequest.getMail(),
                passwordEncoder.encode(regRequest.getPassword()),
                bucket,
                "client"
        );
        bucket.setClient(client);
        clientService.save(client);

    }

}
