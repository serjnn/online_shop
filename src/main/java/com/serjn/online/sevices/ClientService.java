package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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


    @Transactional
    public List<Product> getProductListOfClient(Client client) {
        Bucket bucket = client.getBucket();
        return bucket.getProducts();




    }

    public void changeBalance(Long clienId, int balance) {
        Client client = finById(clienId);
        client.setBalance(balance);
        save(client);
    }

    @Transactional
    public boolean buy() {
        Client client = findCurrentClient();
        List<Product> prods = getProductListOfClient(client);
        int sum = prods.stream().mapToInt(Product::getPrice).sum();

        String prodsIds = prods
                .stream()
                .mapToLong(Product::getId)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));





        if (client.getAddress() != null && client.getBalance() >= sum) {
            OrderDetails orderDetails = new OrderDetails(
                    client.getId(),
                     prodsIds,
                    sum
            );
            orderDetailsService.saveOrder(orderDetails);

            client.setBalance(client.getBalance() - sum);
            Bucket bucket = client.getBucket();
            bucket.getProducts().clear();
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
