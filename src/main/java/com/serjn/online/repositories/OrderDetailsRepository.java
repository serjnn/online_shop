package com.serjn.online.repositories;

import com.serjn.online.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    Optional<OrderDetails> findByClientId(Long clientId);

}
