package com.serjn.online.DTOs;

import java.time.LocalDateTime;

public record OrderDetailsDto(
    Long id,
    LocalDateTime orderDate,
    String status,
    double totalAmount
) {}
