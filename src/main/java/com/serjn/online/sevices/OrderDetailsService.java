package com.serjn.online.sevices;

import com.serjn.online.models.OrderDetails;
import com.serjn.online.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    public void saveOrder(OrderDetails orderDetails){
        orderDetailsRepository.save(orderDetails);
    }

    public OrderDetails findByClientId(Long id){
        return orderDetailsRepository.findByClientId(id)
                .orElseThrow(() -> new NoSuchElementException("No " +
                        "order details with client id: " + id) );


    }
}
