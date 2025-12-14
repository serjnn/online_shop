package com.serjn.online.sevices;

import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import com.serjn.online.model.BucketItem;
import com.serjn.online.model.Client;
import com.serjn.online.model.OrderDetails;
import com.serjn.online.services.BucketItemsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final ClientService clientService;
    private final OrderDetailsService orderDetailsService;

    private final BucketItemsExtractor bucketItemsExtractor;

    @Transactional //TODO separate
    public void purchase() {
        Client client = clientService.findAuthenticatedClient();
        List<BucketItem> bucketItems =
                bucketItemsExtractor.getClientBucketItems(client);
        BigDecimal sum = getSumOfBucket(bucketItems);

        purchaseValidationChecks(client, sum);

        OrderDetails orderDetails = new OrderDetails(
                client.getId(),
                getProductIds(bucketItems),
                sum
        );

        client.setBalance(client.getBalance().subtract(sum));
        client.getBucket().getBucketItems().clear();

        clientService.save(client);
        orderDetailsService.save(orderDetails);
    }


    private BigDecimal getSumOfBucket(List<BucketItem> bucketItems) {
        return bucketItems.stream()
                .map(bucketItem -> bucketItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(bucketItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void purchaseValidationChecks(Client client, BigDecimal sum) {
        try {
            client.getAddress();
        } catch (NullPointerException e) {
            throw new EmptyAddressException("Address is null");
        }
        if (client.getBalance().compareTo(sum) < 0) {
            throw new InsufficientFundsException("Insufficient funds for purchase");
        }

    }

    private String getProductIds(List<BucketItem> bucketItems) {
        return bucketItems
                .stream()
                .mapToLong(i -> i.getProduct().getId())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }


}
