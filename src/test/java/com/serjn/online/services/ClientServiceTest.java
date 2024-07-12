package com.serjn.online.services;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Client;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ClientRepository;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Mock
    private OrderDetailsService orderDetailsService;




    @Test
    public void BucketIsEmptyAfterBuying() {
        Client client = createClient();
        setBucketForClient(client);


        when(clientRepository.save(any(Client.class))).thenReturn(client);


        clientService.buy(client);

        Assertions.assertTrue(client.getBucket().getBucketItems().isEmpty());


    }

    @Test
    public void mailMatch(){
        Client client = createClient();
        Assertions.assertEquals("mail@mail.com", client.getMail());


    }

    private Client createClient() {
        Client client = new Client();
       client.setMail("mail@mail.com");
        client.setBalance(1000);
        client.setAddress("123 Main St");
        return client;
    }

    private void setBucketForClient(Client client) {
        Bucket bucket = new Bucket();

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(100);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(200);

        BucketItems bucketItems1 = new BucketItems();
        bucketItems1.setQuantity(1);
        bucketItems1.setProduct(product1);

        BucketItems bucketItems2 = new BucketItems();
        bucketItems2.setQuantity(3);
        bucketItems2.setProduct(product2);

        List<BucketItems> bucketItemsList = new ArrayList<>();
        bucketItemsList.add(bucketItems1);
        bucketItemsList.add(bucketItems2);

        bucket.setBucketItems(bucketItemsList);

        client.setBucket(bucket);
    }
}
