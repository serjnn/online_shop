package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.OrderDetailsResponse;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.models.*;
import com.serjn.online.sevices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ClientRestController {
    private final OrderDetailsManager orderDetailsManager;
    private final ProductService productService;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;
    private final JwtService jwtService;
    private final DiscountChecker discountChecker;


    @PostMapping("/register")
    public ResponseEntity<?> reg(@RequestBody RegRequest regRequest) {

        if (regRequest.getMail() == null || regRequest.getPassword() == null || regRequest.getRole() == null) {
            return new ResponseEntity<>("Некоторые обязательные поля отсутствуют", HttpStatus.BAD_REQUEST);
        }
        clientService.register(regRequest);
        return ResponseEntity.ok("registered");


    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getMail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {

            return new ResponseEntity<>(new Error(), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = clientDetailService.loadUserByUsername(authRequest.getMail());

        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/clients")
    public List<Client> clients() {
        return clientService.findAll();


    }

    @GetMapping("/secured")
    public String secured() {
        return "SECURED PLACE ";

    }


    @GetMapping("/categories/{cat}")
    public List<Product> bucket(@PathVariable("cat") Category category) {
        return productService.getProductsByCategory(category);


    }
    @GetMapping("/remove/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable("id") Long id) {

        return clientService.reduceProductInCart(id);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<String> addToCart(@PathVariable("id") Long id) {
        clientService.addToBucket(id);
        return ResponseEntity.ok("Product added");
    }

    @GetMapping("/bucket")
    public List<BucketItems> bucket() {
        Client client = clientService.findCurrentClient();
        Bucket bucket = client.getBucket();
        return clientService.getBucketItems(bucket);
    }

    @GetMapping("/buy")
    public ResponseEntity<?> buy() {

        return clientService.buy(clientService.findCurrentClient());

    }


    @GetMapping("/myInfo")
    public Client clientInfo() {
        return clientService.findCurrentClient();
    }

    @PostMapping("/addBalance")
    public void addBalance(@RequestParam Integer amount) {
        clientService.addBalance(amount);

    }

    @PostMapping("/changeAddress")
    public void changeAddress(@RequestParam String address) {
        clientService.setAddress(address);

    }

    @GetMapping("/discount")
    Map<String,Integer> disc(){
        return discountChecker.getDiscountList();
    }

    @GetMapping("/sum")
    int sum (){
        return clientService.getSum(clientService.findCurrentClient().getBucket());
    }

     @GetMapping("/orders")
    List<OrderDetailsResponse> orders(){
        Long clientId = clientService.findCurrentClient().getId();
        return orderDetailsManager.getOrderDetails(clientId);


     }






}
