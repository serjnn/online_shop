package com.serjn.online.sevices.utils;

import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;

    @Transactional
    public ResponseEntity<?> buy() {

        Client client = clientService.findCurrentClient();
        List<BucketItem> bucketItems = getBucketItemsListOfClient();

        int sum = getSumOfBucket(bucketItems);

      Optional<ResponseEntity<?>> clientValidationResult = clientValidationCheck(client,sum);
        if (clientValidationResult.isPresent()) {
            return clientValidationResult.get();
        }

        OrderDetails orderDetails = new OrderDetails(
                client.getId(),
                getProductIds(bucketItems),
                sum
        );

        commitPurchase(client,orderDetails,sum);

        return ResponseEntity.ok(orderDetails);

    }
    @Transactional
    public List<BucketItem> getBucketItemsListOfClient() {
        Bucket bucket = clientService.findCurrentClient().getBucket();
        return bucket.getBucketItems();

    }

    private void commitPurchase(Client client, OrderDetails orderDetails,Integer sum) {
        orderDetailsService.saveOrder(orderDetails);
        client.setBalance(client.getBalance() - sum);
        Bucket bucket = client.getBucket();
        List<BucketItem> list = bucket.getBucketItems();
        list.clear();
        clientService.save(client);

    }

    private int getSumOfBucket(List<BucketItem> bucketItems) {
        return bucketItems.stream().mapToInt(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
    }

    private String getProductIds(List<BucketItem> bucketItems) {
        return bucketItems
                .stream()
                .mapToLong(i -> i.getProduct().getId())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }



    private Optional<ResponseEntity<?>> clientValidationCheck(Client client, int sum) {
        if (client.getAddress() == null) {
            return Optional.of(new ResponseEntity<>("Please enter your address.", HttpStatus.BAD_REQUEST));
        }
        if (client.getBalance() < sum) {
            return Optional.of(new ResponseEntity<>("Not enough money", HttpStatus.BAD_REQUEST));
        }
        return Optional.empty();
    }
}
