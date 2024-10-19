package com.serjn.online.sevices;


import com.serjn.online.models.BucketItems;
import com.serjn.online.repositories.BucketItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BucketItemsService {

    private final BucketItemsRepository bucketItemsRepository;


    public BucketItems findBucketItemByProductId(Long productId){
        return bucketItemsRepository.findBucketItemByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("No bucketItem with product id: " +
                        productId));
    }

}
