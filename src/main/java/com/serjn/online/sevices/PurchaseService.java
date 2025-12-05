package com.serjn.online.sevices;

import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
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

    @Transactional
    public void purchase() {
        Client client = clientService.findAuthenticatedClient();
        List<BucketItem> bucketItems = clientService.findClientBucketItems(client);
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
        int res = bucketItems.stream().mapToInt(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
        return BigDecimal.valueOf(res);
    }

    private void purchaseValidationChecks(Client client, BigDecimal sum) {
        if (client.getAddress().isEmpty()) {
            throw new EmptyAddressException();
        }
        if (client.getBalance().compareTo(sum) < 0) {
            throw new InsufficientFundsException();
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
