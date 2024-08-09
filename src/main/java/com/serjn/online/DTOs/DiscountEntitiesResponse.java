package com.serjn.online.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DiscountEntitiesResponse {
    private Embedded _embedded;

    @Data
    public static class Embedded {
        private List<DiscountEntity> discountEntities;
    }

    @Data
    public static class DiscountEntity {
        private String category;
        private Double discount;
    }
}
