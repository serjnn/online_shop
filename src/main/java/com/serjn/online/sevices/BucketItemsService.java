package com.serjn.online.sevices;


import com.serjn.online.models.BucketItems;
import com.serjn.online.repositories.BucketItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BucketItemsService {
    @Autowired
    BucketItemsRepository bucketItemsRepository;


    public BucketItems findBucketItemByProductId(Long productId){
        return bucketItemsRepository.findBucketItemByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("No bucketItem with product id: " +
                        productId));
    }

    public void save(BucketItems bucketItems) {
        bucketItemsRepository.save(bucketItems);
    }
}
