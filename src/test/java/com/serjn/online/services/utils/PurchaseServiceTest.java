package com.serjn.online.services.utils;


import com.serjn.online.model.Bucket;
import com.serjn.online.model.BucketItem;
import com.serjn.online.model.Client;
import com.serjn.online.model.Product;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import com.serjn.online.sevices.PurchaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private OrderDetailsService orderDetailsService;

    @InjectMocks
    private PurchaseService purchaseService;

    private Client client;


    @BeforeEach
    void setUp() {

        Bucket bucket = new Bucket();

        client = new Client();
        client.setMail("mail@mail.com");
        client.setId(1L);
        client.setBalance(BigDecimal.valueOf(1000));
        client.setAddress("123 Main St");
        client.setBucket(bucket);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(200));

        BucketItem bucketItems1 = new BucketItem();
        bucketItems1.setQuantity(1);
        bucketItems1.setProduct(product1);

        BucketItem bucketItems2 = new BucketItem();
        bucketItems2.setQuantity(3);
        bucketItems2.setProduct(product2);

        bucket.getBucketItems().add(bucketItems1);
        bucket.getBucketItems().add(bucketItems2);

    }

    @Test
    void purchaseSuccessTest() {

        when(clientService.findAuthenticatedClient()).thenReturn(client);

        purchaseService.purchase();

        Assertions.assertEquals(client.getBalance(), BigDecimal.valueOf(300));
        Assertions.assertTrue(client.getBucket().getBucketItems().isEmpty());


    }


}
