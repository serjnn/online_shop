package com.serjn.online.services.utils;


import com.serjn.online.model.entities.Bucket;
import com.serjn.online.model.entities.BucketItem;
import com.serjn.online.model.entities.Client;
import com.serjn.online.model.entities.Product;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import com.serjn.online.sevices.PurchaseService;
import com.serjn.online.sevices.utils.BucketItemsExtractor;
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

    @Mock
    private BucketItemsExtractor bucketItemsExtractor;

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
        when(bucketItemsExtractor.getClientBucketItems(client.getBucket())).thenReturn(client.getBucket().getBucketItems());

        purchaseService.purchase();

        Assertions.assertEquals(BigDecimal.valueOf(300), client.getBalance());
        Assertions.assertTrue(client.getBucket().getBucketItems().isEmpty());


    }


}
