package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.models.*;
import com.serjn.online.sevices.*;
import com.serjn.online.sevices.utils.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ClientRestController {

    private final OrderDetailsService orderDetailsService;
    private final ProductService productService;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final PurchaseService purchaseService;
    private final BucketService bucketService;


    @PostMapping("/register")
     ResponseEntity<HttpStatus> reg(@RequestBody RegRequest regRequest) {
        return clientService.register(regRequest);
    }
//TODO
    @PostMapping("/auth")
     ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getMail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.getMail());

        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/clients")
     List<Client> clients() {
        return clientService.findAll();


    }

    @GetMapping("/secured")
     String secured() {
        return "";

    }


    @GetMapping("/categories/{cat}")
     List<Product> getProductsByCategory(@PathVariable("cat") Category category) {
        return productService.getProductsByCategory(category);


    }


    @GetMapping("/products/{productId}")
     ResponseEntity<HttpStatus> addToCart(@PathVariable("productId") Long productId) {
        return bucketService.addToBucket(productId);

    }

    @GetMapping("/bucket")
     List<BucketItem> getBucketItems(@RequestParam("clientId") Long clientId) {
        return purchaseService.getBucketItemsListOfClient(clientId);
    }
    @GetMapping("/buy")
     ResponseEntity<?> buy(@RequestParam("clientId") Long clientId) {
        return purchaseService.buy(clientId);

    }

    @GetMapping("/getClientsOrderDetails/{clientId}") //changed
     List<OrderDetails> getClientsOrderDetails(@PathVariable("clientId") Long clientId) {
        return orderDetailsService.findOrderDetailsByClientId(clientId);
    }

    @GetMapping("/getClientInfo") //changed
     Client clientInfo(@RequestParam("clientId") Long clientId) {
        return clientService.finById(clientId);
    }

    @PostMapping("/addBalance")
     ResponseEntity<HttpStatus> addBalance(@RequestParam Integer amount) {
        return clientService.addBalance(amount);

    }

    @PostMapping("/changeAddress")
    ResponseEntity<HttpStatus> changeAddress(@RequestParam String address) {
       return  clientService.setAddress(address);

    }


}
