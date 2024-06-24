package com.serjn.online.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orderdetails")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long clientId;
    private String products_ids;
    private int sum;

    public OrderDetails(Long clientId, String products_ids, int sum) {
        this.clientId = clientId;
        this.products_ids = products_ids;
        this.sum = sum;
    }
}