package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItem;
import com.serjn.online.models.Client;
import com.serjn.online.repositories.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final ClientService clientService;
    private final BucketRepository bucketRepository;
    private final ProductService productService;


    public void addToBucket(Long productId) {
        Client client = clientService.findCurrentClient();
        Bucket bucket = client.getBucket();

        BucketItem existingBucketItem = getExistingBucketItem(bucket, productId);

        BucketItem bucketItem;
        if (existingBucketItem != null) {
            existingBucketItem.setQuantity(existingBucketItem.getQuantity() + 1);
        } else {
            bucketItem = new BucketItem(productService.findById(productId), bucket, 1);
            bucket.getBucketItems().add(bucketItem);
        }
        save(bucket);

    }


    public void removeFromBucket(Long productId) {
        Client client = clientService.findCurrentClient();
        Bucket bucket = client.getBucket();
        BucketItem existingBucketItem = getExistingBucketItem(bucket, productId);

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

    private BucketItem getExistingBucketItem(Bucket bucket, Long productId) {
        return bucket
                .getBucketItems()
                .stream()
                .filter(bucketItem -> bucketItem.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);
    }

    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }
}
