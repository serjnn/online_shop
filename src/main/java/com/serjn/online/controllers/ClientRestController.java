package com.serjn.online.controllers;


import com.serjn.online.DTOs.AuthRequest;
import com.serjn.online.DTOs.RegRequest;
import com.serjn.online.JWT.JwtService;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Category;
import com.serjn.online.models.Client;
import com.serjn.online.models.Product;
import com.serjn.online.sevices.ClientDetailService;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClientRestController {

    @Autowired
    ProductService productService;

    @Autowired
    ClientService clientService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ClientDetailService clientDetailService;

    @Autowired
    JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> reg(@RequestBody RegRequest regRequest) {

        if (regRequest.getMail() == null ||
                regRequest.getPassword() == null || regRequest.getRole() == null) {
            return new ResponseEntity<>("Некоторые обязательные поля отсутствуют",
                    HttpStatus.BAD_REQUEST);
        }
        clientService.register(regRequest);
        return ResponseEntity.ok("Success");


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


    @GetMapping("/products/{id}")
    public void addToCart(@PathVariable("id") Long id){
        clientService.addToBucket(id);
    }

    @GetMapping("/bucket")
    public List<BucketItems> bucket(){
        Client client = clientService.findCurrentClient();
        return clientService.getBItemsListOfClient(client);
    }


}
