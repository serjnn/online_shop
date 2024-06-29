package com.serjn.online.sevices;


import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ClientRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    OrderDetailsService orderDetailsService;

    public Client finById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new NoSuchElementException("No client with id: " + clientId));
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

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public Product[] getNoramlCart(Client client) {
        String full = client.getCart();
        String[] arr = full.substring(1, full.length() - 1).split(",");
        return Arrays
                .stream(arr)
                .mapToInt(Integer::valueOf)
                .mapToObj(i -> productService.findById((long) i))
                .toArray(Product[]::new);
    }

    public void changeBalance(Long clienId, int balance) {
        Client client = finById(clienId);
        client.setBalance(balance);
        save(client);
    }
    @Transactional
    public boolean buy() {
        Client client = findCurrentClient();
        Product[] prods = getNoramlCart(client);
        int sum = Arrays.stream(prods).mapToInt(Product::getPrice).sum();

        if (client.getAddress() != null && client.getBalance() >= sum) {
            OrderDetails orderDetails = new OrderDetails(
                    client.getId(),
                    client.getCart().substring(1,
                            client.getCart().length() - 1), // что то не так тут точно
                    sum
            );
            orderDetailsService.saveOrder(orderDetails);

            client.setBalance(client.getBalance() - sum);
            client.setCart("-");
            save(client);
            return true;
        }
        return false;
    }



    public void setAddress(String address) {
        Client client = findCurrentClient();
        client.setAddress(address);
        save(client);
    }
}
