package com.serjn.online.controllers;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.exceptions.InvalidAddressException;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;


    @GetMapping("/findClientsBucket")
    List<BucketItem> findClientsBucket() {
        Client client = clientService.findAuthenticatedClient();
        return clientService.findClientsBucket(client);
    }


    @GetMapping("/findClientInfo")
    ClientDto findClientInfo() {
        return clientService.findClientInfo();
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

    @GetMapping("/secured")
    String secured() {
        return "";
    }




}
