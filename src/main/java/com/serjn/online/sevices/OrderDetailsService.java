package com.serjn.online.sevices;


import com.serjn.online.models.OrderDetails;
import com.serjn.online.repositories.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    private final ClientService clientService;

    public void saveOrder(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }


    public List<OrderDetails> findClientsOrderDetails() {
        Long clientId = clientService.findCurrentClient().getId();
        return orderDetailsRepository.findByClientId(clientId);

    }

}
