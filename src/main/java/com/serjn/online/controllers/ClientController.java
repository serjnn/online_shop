package com.serjn.online.controllers;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Validated
public class ClientController {
    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;

    @GetMapping("/bucket")
    List<BucketItem> findClientsBucket() {
        Client client = clientService.findAuthenticatedClient();
        return clientService.findClientBucketItems(client);
    }

    @GetMapping
    ClientDto findClientInfo() {
        return clientService.findClientInfo();
    }


    @PatchMapping("/address")
    void changeAddress(@Size(min = 10) @RequestBody String address) {
        clientService.setAddress(address);
    }

    @GetMapping("/orders")
    List<OrderDetails> findOrderDetails() {
        return orderDetailsService.findClientsOrderDetails();
    }

}
