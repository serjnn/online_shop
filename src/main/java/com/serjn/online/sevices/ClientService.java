package com.serjn.online.sevices;


import com.serjn.online.models.Client;
import com.serjn.online.repositories.ClientRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;


    public Client findByMail(String mail){
        return clientRepository.findByMail(mail).orElseThrow(()->
                new NoSuchElementException("No client with mail: " +mail));

    }
    public Client findCurrentClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentMail = authentication.getName();
        return findByMail(currentMail);

    }

    public void save(Client client){
        clientRepository.save(client);
    }
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public void deleteById(Long id){
        clientRepository.deleteById(id);
    }
}
