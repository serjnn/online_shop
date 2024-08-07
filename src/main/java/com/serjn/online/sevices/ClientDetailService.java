package com.serjn.online.sevices;

import com.serjn.online.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ClientDetailService implements UserDetailsService {
    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    private ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Client client = clientService.findByMail(mail);

        UserDetails userDetails = User.builder()
                .username(client.getMail())
                .password(client.getPassword())
                .roles(Roles(client.getRole()))
                .build();
        return userDetails;
    }

    private String[] Roles(String role) {
        switch (role) {
            case "client":
                return new String[]{"client"};
            case "manager":
                return new String[]{"manager", "client"};
            case "admin":
                return new String[]{"admin", "manager", "client"};
            default:
                throw new IllegalArgumentException("Unknown role: " + role);

        }
    }
}
