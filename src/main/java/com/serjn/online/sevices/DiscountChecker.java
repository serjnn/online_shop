package com.serjn.online.sevices;


import com.serjn.online.DTOs.DiscountEntitiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DiscountChecker {

    private final RestTemplate restTemplate;

    public Map<String, Integer> getDiscountList() {

        DiscountEntitiesResponse response =
                restTemplate.getForObject("http://localhost:8081/discountEntities", DiscountEntitiesResponse.class);


        if (response != null && response.get_embedded() != null) {

            Map<String, Integer> discountMap = new HashMap<>();


            for (DiscountEntitiesResponse.DiscountEntity entity : response.get_embedded().getDiscountEntities()) {

                discountMap.put(entity.getCategory(), entity.getDiscount().intValue());
            }

            return discountMap;
        }

        return new HashMap<>();
    }
}
