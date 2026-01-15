package com.serjn.online.controllers;


import com.serjn.online.DTOs.BucketItemDto;
import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.DTOs.OrderDetailsDto;
import com.serjn.online.mappers.OrderDetailsMapper;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@Validated
@Tag(name = "Client", description = "Endpoints for client operations")
public class ClientController {
    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;

    @Operation(summary = "Get client bucket", description = "Retrieves the items in the authenticated client's bucket")
    @GetMapping("/bucket")
    List<BucketItemDto> findClientBucket() {
        return clientService.findClientBucket();
    }

    @Operation(summary = "Get client info", description = "Retrieves information about the authenticated client")
    @GetMapping
    ClientDto findClientInfo() {
        return clientService.findClientInfo();
    }

    @Operation(summary = "Change address", description = "Updates the address of the authenticated client")
    @PatchMapping("/address")
    void changeAddress(@Size(min = 10) @RequestBody String address) {
        clientService.setAddress(address);
    }

    @Operation(summary = "Get order details", description = "Retrieves the order history of the authenticated client")
    @GetMapping("/orders")
    List<OrderDetailsDto> findOrderDetails() {
        return OrderDetailsMapper.INSTANCE.toDtoList(orderDetailsService.findClientsOrderDetails());
    }

}
