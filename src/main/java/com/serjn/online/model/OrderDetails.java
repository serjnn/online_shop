package com.serjn.online.model;

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
    private String products_ids;
    private BigDecimal sum;

    private LocalDateTime created_at;


    public OrderDetails(Long clientId, String products_ids, BigDecimal sum) {
        this.clientId = clientId;
        this.products_ids = products_ids;
        this.sum = sum;
        this.created_at = LocalDateTime.now();
    }
}