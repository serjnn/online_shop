package com.serjn.online.repositories;

import com.serjn.online.models.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketItemRepository extends JpaRepository<BucketItem,Long> {

    Optional<BucketItem> findBucketItemByProductId(Long productId);
}
