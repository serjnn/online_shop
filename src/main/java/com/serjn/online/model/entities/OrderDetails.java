package com.serjn.online.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "product_id")
    private List<Long> productIds;

    private BigDecimal sum;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public OrderDetails(Long clientId, List<Long> productIds, BigDecimal sum) {
        this.clientId = clientId;
        this.productIds = productIds;
        this.sum = sum;
        this.createdAt = LocalDateTime.now();
    }
}