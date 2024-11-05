package com.serjn.online.sevices.utils;

import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;

    @Transactional
    public void purchase() {

        Client client = clientService.findCurrentClient();
        List<BucketItem> bucketItems = getBucketItemsListOfClient();
        BigDecimal sum = getSumOfBucket(bucketItems);

        purchaseValidationChecks(client, sum);

        OrderDetails orderDetails = new OrderDetails(
                client.getId(),
                getProductIds(bucketItems),
                sum
        );

        commitPurchase(client, orderDetails, sum);


    }

    private void purchaseValidationChecks(Client client, BigDecimal sum) {
        if (client.getAddress().isEmpty()) {
            throw new EmptyAddressException();
        }
        if (client.getBalance().compareTo(sum) < 0) {
            throw new InsufficientFundsException();
        }

    }

    @Transactional
    public List<BucketItem> getBucketItemsListOfClient() {
        Bucket bucket = clientService.findCurrentClient().getBucket();
        return bucket.getBucketItems();

    }

    private void commitPurchase(Client client, OrderDetails orderDetails, BigDecimal sum) {
        orderDetailsService.saveOrder(orderDetails);
        client.setBalance(client.getBalance().subtract(sum));
        Bucket bucket = client.getBucket();
        List<BucketItem> list = bucket.getBucketItems();
        list.clear();
        clientService.save(client);

    }

    private BigDecimal getSumOfBucket(List<BucketItem> bucketItems) {
        int res = bucketItems.stream().mapToInt(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
        return BigDecimal.valueOf(res);
    }

    private String getProductIds(List<BucketItem> bucketItems) {
        return bucketItems
                .stream()
                .mapToLong(i -> i.getProduct().getId())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }


}
