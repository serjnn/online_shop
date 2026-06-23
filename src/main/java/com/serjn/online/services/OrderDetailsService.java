package com.serjn.online.services;


import com.serjn.online.model.entities.OrderDetails;
import com.serjn.online.repositories.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final ClientService clientService;

    @Transactional
    public void save(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }


    public List<OrderDetails> findClientsOrderDetails() {
        Long clientId = clientService.findAuthenticatedClient().getId();
        return orderDetailsRepository.findByClientId(clientId);

    }

}
