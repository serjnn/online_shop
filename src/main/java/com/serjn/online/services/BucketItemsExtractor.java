package com.serjn.online.services;

import com.serjn.online.model.Bucket;
import com.serjn.online.model.BucketItem;
import com.serjn.online.model.Client;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BucketItemsExtractor {

    @Transactional public List<BucketItem> getClientBucketItems(Client client) {
        Bucket bucket = client.getBucket();
        return bucket.getBucketItems();
    }

    @Transactional
    public List<BucketItem> getClientBucketItemsExistingData(Client client, Bucket bucket) {
        return bucket.getBucketItems();
    }
}
