package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.exceptions.AuthFailedException;
import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import com.serjn.online.exceptions.InvalidAddressException;
import com.serjn.online.models.BucketItem;
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

import java.math.BigDecimal;
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
    ResponseEntity<String> reg(@RequestBody RegRequest regRequest) {
        try {
            authHandler.register(regRequest);
        } catch (AuthFailedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Register failed");
        }
        return null;
    }


    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody AuthRequest authRequest) {
        try {
            authHandler.auth(authRequest);
        } catch (AuthFailedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Auth failed");
        }
        return null;
    }

    @GetMapping("/bucketItems")
    List<BucketItem> getBucketItems() {
        return purchaseService.getBucketItemsListOfClient();
    }

    @GetMapping("/purchase")
    ResponseEntity<String> purchase() {
        try {
            purchaseService.purchase();
        } catch (EmptyAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty address");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough money");

        }
        return null;

    }

    @GetMapping("/orderDetails/{clientId}")
    List<OrderDetails> getClientsOrderDetails() {
        return orderDetailsService.findClientsOrderDetails();
    }

    @GetMapping("/clientInfo")
    ClientDto clientInfo() {
        return clientService.getTransferClient();
    }

    @PostMapping("/addBalance")
    ResponseEntity<HttpStatus> addBalance(@RequestParam BigDecimal amount) {
        return clientService.addBalance(amount);

    }

    @PostMapping("/changeAddress")
    ResponseEntity<String> changeAddress(@RequestParam String address) {
        try {
            clientService.setAddress(address);
        } catch (InvalidAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid address");
        }
        return null;
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
