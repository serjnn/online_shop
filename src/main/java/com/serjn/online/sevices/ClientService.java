package com.serjn.online.sevices;


import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    public Client finById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("No client with id: " + clientId));
    }


    public Client findByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No client with mail: " + mail));

    }


    public Client findCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.getUsername();
        String currentMail = authentication.getName();
        return findByMail(currentMail);

    }

    public void save(Client client) {
        clientRepository.save(client);
    }


    public ResponseEntity<HttpStatus> addBalance(int balance) {
        Client client = findCurrentClient();
        client.setBalance(client.getBalance() + balance);
        save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<HttpStatus> setAddress(String address) {
        //TODO address validation

        Client client = findCurrentClient();
        client.setAddress(address);
        save(client);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
