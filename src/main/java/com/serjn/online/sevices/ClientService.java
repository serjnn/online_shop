package com.serjn.online.sevices;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.mappers.ClientMapper;
import com.serjn.online.model.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client findClientByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No such client with mail: " + mail));
    }

    public Client findAuthenticatedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();
        return findClientByMail(mail);
    }


    public void setAddress(String address) {
        Client client = findAuthenticatedClient();
        client.setAddress(address);
        save(client);
    }


    public ClientDto findClientInfo() {
        Client client = findAuthenticatedClient();
        return ClientMapper.INSTANCE.toDto(client);
    }


    public void save(Client client) {
        clientRepository.save(client);
    }

}
