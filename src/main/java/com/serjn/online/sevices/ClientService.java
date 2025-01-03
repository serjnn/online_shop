package com.serjn.online.sevices;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.exceptions.InvalidAddressException;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String currentMail = authentication.getName();
        return findByMail(currentMail);

    }

    public void save(Client client) {
        clientRepository.save(client);
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
        return new ClientDto(client.getId(),
                client.getMail(),
                client.getAddress(),
                client.getBalance());

    }


    @Transactional(readOnly = true)
    public List<BucketItem> getBucketItemsListOfClient(Client client) {
        Bucket bucket = client.getBucket();
        return bucket.getBucketItems();

    }

    @Transactional(readOnly = true)
    public Bucket test() {
        Client client =findCurrentClient();
        return client.getBucket();

    }

}
