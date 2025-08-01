package com.serjn.online.sevices;


import com.serjn.online.DTOs.ClientDto;
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

    public Client findClientByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No client with mail: " + mail));
    }

    public Client findAuthenticatedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();
        return findClientByMail(mail);

    }

    public void save(Client client) {
        clientRepository.save(client);
    }


    public void setAddress(String address) {
        //TODO is valid

        Client client = findAuthenticatedClient();
        client.setAddress(address);
        save(client);
    }


    public ClientDto findClientInfo() {
        Client client = findAuthenticatedClient();
        return new ClientDto(client.getId(),
                client.getMail(),
                client.getAddress(),
                client.getBalance());

    }


    @Transactional(readOnly = true)
    public List<BucketItem> findClientsBucket(Client client) {
        Bucket bucket = client.getBucket();
        return bucket.getBucketItems();

    }


}
