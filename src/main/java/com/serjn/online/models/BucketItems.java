package com.serjn.online.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "bucketItems")
public class BucketItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "bucket_id", nullable = false)
    private Bucket bucket;

    private int quantity;

    public BucketItems(Product product, Bucket bucket, int quantity) {
        this.product = product;
        this.bucket = bucket;
        this.quantity = quantity;
    }
}
