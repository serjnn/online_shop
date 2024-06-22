package com.serjn.online.sevices;


import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;


    public Client findByMail(String mail){
        return clientRepository.findByMail(mail).orElseThrow(()->
                new NoSuchElementException("No client with mail: " +mail));

    }

    public void save(Client client){
        clientRepository.save(client);
    }

}
