package com.serjn.online.repositories;

import com.serjn.online.models.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket,Long> {
    Optional<Bucket> findBucketByClientId(Long userId);


}
