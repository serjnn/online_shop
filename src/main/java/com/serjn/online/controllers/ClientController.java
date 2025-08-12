package com.serjn.online.controllers;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
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


    @GetMapping("/changeAddress")
    void changeAddress(@Size(min = 10)  @RequestParam String address) {
        clientService.setAddress(address);
    }

    @GetMapping("/secured")
    String secured() {
        return "";
    }


}
