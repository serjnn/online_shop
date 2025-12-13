package com.serjn.online.controllers;


import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.model.BucketItem;
import com.serjn.online.model.Client;
import com.serjn.online.model.OrderDetails;
import com.serjn.online.services.BucketItemsExtractor;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@Validated
public class ClientController {
    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;
    private final BucketItemsExtractor bucketItemsExtractor;

    @GetMapping("/bucket")
    List<BucketItem> findClientBucket() {
        Client client = clientService.findAuthenticatedClient();
        return bucketItemsExtractor.getClientBucketItems(client);
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
