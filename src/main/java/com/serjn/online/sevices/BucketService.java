package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.repositories.BucketItemRepository;
import com.serjn.online.repositories.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final ClientService clientService;
    private final BucketRepository bucketRepository;
    private final BucketItemRepository bucketItemRepository;
    private final ProductService productService;


    public Bucket findBucketByClientId(Long clientId) {

        return bucketRepository.findBucketByClientId(clientId)
                .orElseGet(() -> createBucket(clientId));

    }

    private Bucket createBucket(Long clientId) { //TODO CONSIDER bucket CREaTES TWICE
        Client client = clientService.finById(clientId);
        Bucket bucket = new Bucket(client);
        client.setBucket(bucket);
        clientService.save(client);
        return bucketRepository.save(bucket);
    }

    public ResponseEntity<HttpStatus> addToBucket(Long productId) {
        Client client = clientService.findCurrentClient();
        Bucket bucket = findBucketByClientId(client.getId());

        Optional<BucketItem> existingBucketItem = bucket
                .getBucketItems()
                .stream()
                .filter(bucketItem -> bucketItem.getProduct().getId() == productId)
                .findFirst();

        BucketItem bucketItem;
        if (existingBucketItem.isPresent()) {
            bucketItem = bucketItemRepository.findBucketItemByProductId(productId).orElseThrow();
            bucketItem.setQuantity(bucketItem.getQuantity() + 1);
        } else {
            bucketItem = new BucketItem(productService.findById(productId), bucket, 1);

            bucket.getBucketItems().add(bucketItem);

        }
        save(bucket);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }
}
