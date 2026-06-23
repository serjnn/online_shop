package com.serjn.online.services;

import com.serjn.online.model.entities.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientDetailService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Client client = clientRepository.findByMail(mail).orElseThrow(() ->
                new UsernameNotFoundException("No client with mail: " + mail));

        return User.builder()
                .username(client.getMail())
                .password(client.getPassword())
                .roles(client.getRole())
                .build();
    }


}
