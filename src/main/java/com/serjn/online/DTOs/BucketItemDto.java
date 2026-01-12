package com.serjn.online.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BucketItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
