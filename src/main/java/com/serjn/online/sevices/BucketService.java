package com.serjn.online.sevices;


import com.serjn.online.model.Bucket;
import com.serjn.online.model.BucketItem;
import com.serjn.online.model.Client;
import com.serjn.online.repositories.BucketRepository;
import com.serjn.online.sevices.utils.BucketItemsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final ClientService clientService;
    private final BucketRepository bucketRepository;
    private final ProductService productService;

    private final BucketItemsExtractor bucketItemsExtractor;


    public void addProductToBucket(Long productId) {
        Client client = clientService.findAuthenticatedClient();
        Bucket bucket = client.getBucket();
        List<BucketItem> bucketItems = bucketItemsExtractor.getClientBucketItemsExistingData(client, bucket);
        BucketItem existingBucketItem = findExistingBucketItem(bucketItems, productId);


        if (existingBucketItem != null) {
            existingBucketItem.setQuantity(existingBucketItem.getQuantity() + 1);
        } else {
            BucketItem bucketItem = new BucketItem(productService.findById(productId), bucket, 1);
            bucket.getBucketItems().add(bucketItem);
        }
        save(bucket);

    }


    public void removeProductFromBucket(Long productId) {
        Client client = clientService.findAuthenticatedClient();
        Bucket bucket = client.getBucket();
        List<BucketItem> bucketItems = bucket.getBucketItems();

        BucketItem existingBucketItem = findExistingBucketItem(bucketItems, productId);

        if (existingBucketItem == null) {
            return;
        }
        if (existingBucketItem.getQuantity() > 1) {
            existingBucketItem.setQuantity(existingBucketItem.getQuantity() - 1);
        } else {
            bucket.getBucketItems().remove(existingBucketItem);
        }
        save(bucket);


    }

    private BucketItem findExistingBucketItem(List<BucketItem> bucketItems, Long productId) {
        return bucketItems
                .stream()
                .filter(bucketItem -> bucketItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }

}
