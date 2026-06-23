package com.serjn.online.services;


import com.serjn.online.model.dto.BucketItemDto;
import com.serjn.online.model.dto.ClientDto;
import com.serjn.online.mappers.BucketItemMapper;
import com.serjn.online.mappers.ClientMapper;
import com.serjn.online.model.entities.Client;
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
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BucketItemMapper bucketItemMapper;

    public Client findClientByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() ->
                new NoSuchElementException("No such client with mail: " + mail));
    }

    public Client findAuthenticatedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();
        return findClientByMail(mail);
    }


    @Transactional
    public void setAddress(String address) {
        Client client = findAuthenticatedClient();
        client.setAddress(address);
        save(client);
    }


    public ClientDto findClientInfo() {
        Client client = findAuthenticatedClient();
        return clientMapper.toDto(client);
    }


    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }

    public List<BucketItemDto> findClientBucket() {
        Client client = findAuthenticatedClient();
        return bucketItemMapper.toDtoList(client.getBucket().getBucketItems());
    }
}
