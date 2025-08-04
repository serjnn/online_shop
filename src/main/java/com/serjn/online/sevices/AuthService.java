package com.serjn.online.sevices;


import com.serjn.online.DTOs.AuthRequestDto;
import com.serjn.online.DTOs.RegisterRequestDto;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
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


    public String auth(@RequestBody AuthRequestDto authRequest) {
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

    public void register(RegisterRequestDto registerRequestDto) {

        Bucket bucket = new Bucket();

        Client client = new Client(
                registerRequestDto.getMail(),
                passwordEncoder.encode(registerRequestDto.getPassword()),
                bucket,
                "client"
        );
        bucket.setClient(client);
        clientService.save(client);

    }

}
