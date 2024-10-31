package com.serjn.online.sevices;

import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class ClientDetailService implements UserDetailsService {

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Client client = clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No client with mail: " + mail));

        return User.builder()
                .username(client.getMail())
                .password(client.getPassword())
                .roles(Roles(client.getRole()))
                .build();
    }

    private String[] Roles(String role) {
        return switch (role) {
            case "client" -> new String[]{"client"};
            case "manager" -> new String[]{"manager", "client"};
            case "admin" -> new String[]{"admin", "manager", "client"};
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
