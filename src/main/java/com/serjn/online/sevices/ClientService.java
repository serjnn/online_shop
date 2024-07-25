package com.serjn.online.sevices;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProductService productService;
    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BucketService bucketService;

    @Autowired
    BucketItemsService bucketItemsService;

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
    public List<BucketItems> getBItemsListOfClient(Client client) {
        Bucket bucket = client.getBucket();
        return bucket.getBucketItems();


    }

    public void changeBalance(Long clienId, int balance) {
        Client client = finById(clienId);
        client.setBalance(balance);
        save(client);
    }

    @Transactional
    public boolean buy(Client client) {
        List<BucketItems> bitems = getBItemsListOfClient(client);
        int sum = bitems.stream().mapToInt(i -> i.getProduct().getPrice() *
                i.getQuantity()).sum();

        String strIDS = bitems.stream()
                .mapToLong(i -> i.getProduct().getId())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));


        if (client.getAddress() != null && client.getBalance() >= sum) {
            OrderDetails orderDetails = new OrderDetails(
                    client.getId(),
                    strIDS,
                    sum
            );
            orderDetailsService.saveOrder(orderDetails);

            client.setBalance(client.getBalance() - sum);
            Bucket bucket = client.getBucket();
            List<BucketItems> list = bucket.getBucketItems();
            list.clear();

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

    public void register(RegRequest regRequest) {
        Bucket bucket = new Bucket();

        Client client = new Client(regRequest.getMail(),
                passwordEncoder.encode(regRequest.getPassword()),
                bucket,
                regRequest.getRole().toLowerCase());
        bucket.setClient(client);
        save(client);

    }

    public void auth(AuthRequest authRequest) {


    }

    public ResponseEntity<String> addToBucket(Long productId) {
        Client client = findCurrentClient();
        Bucket bucket = bucketService.findBucketByClientId(client.getId());

        Optional<BucketItems> existingBucketItem =bucket.getBucketItems()
                .stream()
                .filter(bucketItem -> bucketItem.getProduct().getId() == productId)
                .findFirst();

        if (existingBucketItem.isPresent()) {
            BucketItems bucketItems = bucketItemsService.findBucketItemByProductId(productId);
            bucketItems.setQuantity(bucketItems.getQuantity() + 1);
            bucketService.save(bucket);
            return ResponseEntity.ok("Product has added");
        }
        else {
            BucketItems bucketItems = new BucketItems(
                    productService.findById(productId),
                    bucket,
                    1);

            bucket.getBucketItems().add(bucketItems);
            bucketService.save(bucket);
            return ResponseEntity.ok("Product has added");

    }

}
}
