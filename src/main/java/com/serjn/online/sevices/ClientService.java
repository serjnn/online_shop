package com.serjn.online.sevices;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.exceptions.InvalidAddressException;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


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


    public ResponseEntity<HttpStatus> addBalance(BigDecimal balance) {
        Client client = findCurrentClient();
        client.setBalance(client.getBalance().add(balance));
        save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public void setAddress(String address) {
        if (!isValidAddress(address)) {
            throw new InvalidAddressException();
        }

        Client client = findCurrentClient();
        client.setAddress(address);
        save(client);

    }

    private boolean isValidAddress(String address) {
        return address.length() > 5 && address.contains(" ");

    }


    public ClientDto getTransferClient() {
        Client client = findCurrentClient();
        return new ClientDto(client.getId()
                , client.getMail(),
                client.getAddress(),
                client.getBalance());

    }

    public void clearBucket(Client client) {
        Bucket bucket = client.getBucket();
        List<BucketItem> list = bucket.getBucketItems();
        list.clear();
    }

    public void deductMoney(Client client, BigDecimal sum) {
        client.setBalance(client.getBalance().subtract(sum));
        save(client);
    }
}
