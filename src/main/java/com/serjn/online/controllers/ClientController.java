package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import com.serjn.online.sevices.utils.AuthHandler;
import com.serjn.online.sevices.utils.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {
    private final BucketService bucketService;
    private final OrderDetailsService orderDetailsService;
    private final ClientService clientService;
    private final PurchaseService purchaseService;
    private final AuthHandler authHandler;

    @PostMapping("/register")
    ResponseEntity<HttpStatus> reg(@RequestBody RegRequest regRequest) {
        return authHandler.register(regRequest);
    }

    //TODO
    @PostMapping("/auth")
    ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        return authHandler.auth(authRequest);
    }

    @GetMapping("/bucketItems")
    List<BucketItem> getBucketItems() {
        return purchaseService.getBucketItemsListOfClient();
    }

    @GetMapping("/buy")
    ResponseEntity<?> buy() {
        return purchaseService.buy();

    }

    @GetMapping("/orderDetails/{clientId}")
        //changed
    List<OrderDetails> getClientsOrderDetails(@PathVariable("clientId") Long clientId) {
        return orderDetailsService.findOrderDetailsByClientId(clientId);
    }

    @GetMapping("/clientInfo")
        //changed
    Client clientInfo() {
        return clientService.findCurrentClient();
    }

    @PostMapping("/addBalance")
    ResponseEntity<HttpStatus> addBalance(@RequestParam Integer amount) {
        return clientService.addBalance(amount);

    }

    @PostMapping("/changeAddress")
    ResponseEntity<HttpStatus> changeAddress(@RequestParam String address) {
        return clientService.setAddress(address);

    }

    @GetMapping("/addProduct/{productId}")
    ResponseEntity<HttpStatus> addToBucket(@PathVariable("productId") Long productId) {
        return bucketService.addToBucket(productId);
    }

    @GetMapping("/removeProduct/{productId}")
    ResponseEntity<HttpStatus> removeFromBucket(@PathVariable("productId") Long productId) {
        return bucketService.removeFromBucket(productId);
    }


}
