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
import org.springframework.http.HttpStatus;
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
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setBucketService(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @Autowired
    public void setBucketItemsService(BucketItemsService bucketItemsService) {
        this.bucketItemsService = bucketItemsService;
    }

    private ClientRepository clientRepository;

    private ProductService productService;

    private OrderDetailsService orderDetailsService;

    private PasswordEncoder passwordEncoder;


    private BucketService bucketService;


    private BucketItemsService bucketItemsService;

    public Client finById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("No client with id: " + clientId));
    }


    public Client findByMail(String mail) {
        return clientRepository.findByMail(mail).orElseThrow(() -> new NoSuchElementException("No client with mail: " + mail));

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

    public void addBalance(int balance) {
        Client client = findCurrentClient();
        client.setBalance(client.getBalance() + balance);
        save(client);
    }

    @Transactional
    public ResponseEntity<?> buy(Client client) {

        if (client.getAddress() == null)
            return new ResponseEntity<>("Please enter ur address.", HttpStatus.BAD_REQUEST);


        List<BucketItems> bitems = getBItemsListOfClient(client);
        int sum = bitems.stream().mapToInt(i -> i.getProduct().getPrice() * i.getQuantity()).sum();


        if (client.getBalance() < sum) return new ResponseEntity<>("Not enough money", HttpStatus.BAD_REQUEST);


        String strIDS = bitems.stream().mapToLong(i -> i.getProduct().getId()).mapToObj(String::valueOf).collect(Collectors.joining(","));


        OrderDetails orderDetails = new OrderDetails(client.getId(), strIDS, sum);
        orderDetailsService.saveOrder(orderDetails);

        client.setBalance(client.getBalance() - sum);
        Bucket bucket = client.getBucket();
        List<BucketItems> list = bucket.getBucketItems();
        list.clear();

        save(client);


        return ResponseEntity.ok(orderDetails);

    }


    public void setAddress(String address) {
        Client client = findCurrentClient();
        client.setAddress(address);
        save(client);
    }

    public void register(RegRequest regRequest) {
        Bucket bucket = new Bucket();

        Client client = new Client(regRequest.getMail(), passwordEncoder.encode(regRequest.getPassword()), bucket, regRequest.getRole().toLowerCase());
        bucket.setClient(client);
        save(client);

    }

    public void auth(AuthRequest authRequest) {


    }

    public ResponseEntity<String> addToBucket(Long productId) {
        Client client = findCurrentClient();
        Bucket bucket = bucketService.findBucketByClientId(client.getId());

        Optional<BucketItems> existingBucketItem = bucket.getBucketItems().stream().filter(bucketItem -> bucketItem.getProduct().getId() == productId).findFirst();

        if (existingBucketItem.isPresent()) {
            BucketItems bucketItems = bucketItemsService.findBucketItemByProductId(productId);
            bucketItems.setQuantity(bucketItems.getQuantity() + 1);
            bucketService.save(bucket);
            return ResponseEntity.ok("Product has added");
        } else {
            BucketItems bucketItems = new BucketItems(productService.findById(productId), bucket, 1);

            bucket.getBucketItems().add(bucketItems);
            bucketService.save(bucket);
            return ResponseEntity.ok("Product has added");

        }

    }
}
