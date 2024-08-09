package com.serjn.online.sevices;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.DetailsBody;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Client;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class ClientService {


    @Autowired
    public void setDiscountChecker(DiscountChecker discountChecker) {
        this.discountChecker = discountChecker;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderDetailsManager(OrderDetailsManager orderDetailsManager) {
        this.orderDetailsManager = orderDetailsManager;
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

    private DiscountChecker discountChecker;
    private OrderDetailsManager orderDetailsManager;
    private ClientRepository clientRepository;

    private ProductService productService;

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
    public List<BucketItems> getBucketItems(Bucket bucket) {
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

        Bucket bucket = client.getBucket();
        int sum = getSum(bucket);
        DetailsBody detailsBody = new DetailsBody(client.getId(), getIDS(bucket), sum);
        orderDetailsManager.sendOrderDetails(detailsBody);


        if (client.getBalance() < sum) return new ResponseEntity<>("Not enough money", HttpStatus.BAD_REQUEST);
        //TODO make own exception class with @ResponseStatus and use it

        client.setBalance(client.getBalance() - sum);
        bucket.getBucketItems().clear();
        save(client);

        return ResponseEntity.ok(detailsBody);

    }


    private String getIDS(Bucket bucket) {
        StringBuilder sb = new StringBuilder();
        for (BucketItems item : bucket.getBucketItems()) {
            sb.append(item.getProduct().getName()).append(":").append(item.getQuantity()).append("|");
        }
        return sb.toString();
    }

    public int getSum(Bucket bucket) {

        List<BucketItems> bitems = getBucketItems(bucket);
        Map<String, Integer> discounts = discountChecker.getDiscountList();
        int sum = 0;
        for (BucketItems item : bitems) {
            String cat = item.getProduct().getCategory().toString();
            if (discounts.containsKey(cat)) {
                double percent = (double) discounts.get(cat) / 100;
                sum += (int) Math.floor((item.getProduct().getPrice() * (1 - percent)) * item.getQuantity());
            } else {
                sum += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return sum;

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
        Bucket bucket = client.getBucket();

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

    public ResponseEntity<String> reduceProductInCart(Long id) {
        System.out.println("______________________________________");
        System.out.println(id);
        Client client = findCurrentClient();
        Bucket bucket = client.getBucket();
        List<BucketItems> list = getBucketItems(bucket);
        BucketItems item = list.stream().filter(i -> i.getProduct().getId() == id).findFirst().orElse(null);
        if (item == null) return new ResponseEntity<>("No such item", HttpStatus.BAD_REQUEST);
        item.setQuantity(item.getQuantity()-1);

        bucketItemsService.save(item);
        bucketService.save(bucket);
        return new ResponseEntity<>("Item decreased", HttpStatus.ACCEPTED);


    }
}
