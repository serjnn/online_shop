package com.serjn.online.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailsBody {
    private Long clientId;
    private String productIds;
    private int sum;
}
