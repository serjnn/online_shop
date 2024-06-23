package com.serjn.online.sevices;


import com.serjn.online.models.Client;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ClientRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@SessionScope
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProductService productService;


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

    public Product [] getNoramlCart(Client client){
        String full = client.getCart();
        String [] arr = full.substring(1,full.length()-1).split(",");
        return   Arrays
                .stream(arr)
                .mapToInt(Integer::valueOf)
                .mapToObj(i -> productService.findById((long) i))
                .toArray(Product[]::new);
    }
}
