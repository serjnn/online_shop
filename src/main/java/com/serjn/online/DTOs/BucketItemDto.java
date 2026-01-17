package com.serjn.online.DTOs;

import java.math.BigDecimal;

public record BucketItemDto(
    Long productId,
    String productName,
    int quantity,
    BigDecimal price
) {}
