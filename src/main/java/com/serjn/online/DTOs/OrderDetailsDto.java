package com.serjn.online.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailsDto {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private double totalAmount;
}
