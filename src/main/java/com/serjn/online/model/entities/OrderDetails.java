package com.serjn.online.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long clientId;

    @Column(name = "products_ids")
    private String productIds;

    private BigDecimal sum;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public OrderDetails(Long clientId, String productIds, BigDecimal sum) {
        this.clientId = clientId;
        this.productIds = productIds;
        this.sum = sum;
        this.createdAt = LocalDateTime.now();
    }
}