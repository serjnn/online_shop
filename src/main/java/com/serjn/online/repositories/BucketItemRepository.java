package com.serjn.online.repositories;

import com.serjn.online.models.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketItemRepository extends JpaRepository<BucketItem,Long> {

}
