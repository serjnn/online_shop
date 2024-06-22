package com.serjn.online.repositories;

import com.serjn.online.models.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket,Long> {
}
