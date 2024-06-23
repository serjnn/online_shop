package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BucketService {
    @Autowired
    BucketRepository bucketRepository;

    public Bucket  findBucketByClientId(Long userId){
        return bucketRepository.findBucketByClientId(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No such bucket with userId: " + userId
                ));
    }
    public void save(Bucket bucket){
        bucketRepository.save(bucket);
    }
}
