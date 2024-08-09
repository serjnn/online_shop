package com.serjn.online.sevices;


import com.serjn.online.DTOs.DetailsBody;
import com.serjn.online.DTOs.OrderDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDetailsManager {
    private final RestTemplate restTemplate;

    public ResponseEntity<HttpStatus> sendOrderDetails(DetailsBody detailsBody) {
        String url = "http://localhost:8082/api/v1/orderdetails";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<DetailsBody> requestEntity = new HttpEntity<>(detailsBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return new ResponseEntity<>(response.getStatusCode());


    }

    public List<OrderDetailsResponse> getOrderDetails(Long id) {
        String url = "http://localhost:8082/api/v1/orderdetails/byClient/" + id;
        OrderDetailsResponse[] response = restTemplate.getForObject(url, OrderDetailsResponse[].class);
        assert response != null;
        return List.of(response);
    }
}


