package com.serjn.online.DTOs;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailsResponse {
    private Long id;
    private Long clientId;
    private String products_ids;
    private int sum;
    private String created_at;

}
