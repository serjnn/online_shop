package com.serjn.online.sevices;


import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;


    public Client finById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("No client with id: " + clientId));
    }


    public Client findByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No client with mail: " + mail));

    }

    public Client findCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentMail = authentication.getName();
        return findByMail(currentMail);

    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
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
        save(client);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
