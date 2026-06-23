package com.serjn.online.controllers;


import com.serjn.online.model.dto.BucketItemDto;
import com.serjn.online.model.dto.ClientDto;
import com.serjn.online.model.dto.OrderDetailsDto;
import com.serjn.online.mappers.OrderDetailsMapper;
import com.serjn.online.services.ClientService;
import com.serjn.online.services.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import com.serjn.online.model.dto.AddressRequestDto;
import jakarta.validation.Valid;
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
    private final OrderDetailsMapper orderDetailsMapper;

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
    void changeAddress(@Valid @RequestBody AddressRequestDto addressRequest) {
        clientService.setAddress(addressRequest.address());
    }

    @Operation(summary = "Get order details", description = "Retrieves the order history of the authenticated client")
    @GetMapping("/orders")
    List<OrderDetailsDto> findOrderDetails() {
        return orderDetailsMapper.toDtoList(orderDetailsService.findClientsOrderDetails());
    }

}
