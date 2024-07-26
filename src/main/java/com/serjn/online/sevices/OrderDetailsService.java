package com.serjn.online.sevices;

import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ClientService clientService;

    public void saveOrder(OrderDetails orderDetails){
        orderDetailsRepository.save(orderDetails);
    }



    public List<OrderDetails> findDetailsOfCurrentClient(){
        Client client = clientService.findCurrentClient();
        return orderDetailsRepository.findByClientId(client.getId());

    }
    public void deleteALl() {
        orderDetailsRepository.deleteAll();
    }
}
