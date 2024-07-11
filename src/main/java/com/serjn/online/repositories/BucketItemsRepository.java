package com.serjn.online.repositories;

import com.serjn.online.models.BucketItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketItemsRepository extends JpaRepository<BucketItems,Long> {

    Optional<BucketItems> findBucketItemByProductId(Long productId);
}
