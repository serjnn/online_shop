package com.serjn.online.sevices.utils;

import com.serjn.online.model.Bucket;
import com.serjn.online.model.BucketItem;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class BucketItemsExtractor {

    public List<BucketItem> getClientBucketItems(Bucket bucket) {
        return bucket.getBucketItems();
    }



}
