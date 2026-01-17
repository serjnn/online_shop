package com.serjn.online.ITests.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serjn.online.DTOs.AuthRequestDto;
import com.serjn.online.DTOs.RegisterRequestDto;
import com.serjn.online.model.Category;
import com.serjn.online.model.Product;
import com.serjn.online.repositories.ClientRepository;
import com.serjn.online.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserBucketITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    private String token;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() throws Exception {
        String userMail = "test@example.com";
        String userPassword = "password12345";

        String address = "123 some 123 some";

        RegisterRequestDto registerRequest = new RegisterRequestDto(userMail, userPassword);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthRequestDto authRequest = new AuthRequestDto(userMail, userPassword);

        MvcResult result = mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        //taking token for further requests
        this.token = result.getResponse().getContentAsString();

        //setting address
        mockMvc.perform(patch("/api/v1/me/address")
                        .header("Authorization", "Bearer " + this.
                                token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(address))
                .andExpect(status().isOk());


        product1 = productRepository.save(
                new Product("Laptop", "Desc", new BigDecimal("50.00"),
                        Category.ELECTRONICS));
        product2 = productRepository.save(
                new Product("Toy", "Desc", new BigDecimal("25.00"),
                        Category.TOYS));
    }


    @Test
    void shouldMatchAddedProducts() throws Exception {
        fillBucket();

        mockMvc.perform(get("/api/v1/me/bucket")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product2.getId()))
                .andExpect(
                        jsonPath("$[0].productName").value(product2.getName()))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].price").value(
                        product2.getPrice().doubleValue()))
                .andExpect(jsonPath("$[1].productId").value(product1.getId()))
                .andExpect(
                        jsonPath("$[1].productName").value(product1.getName()))
                .andExpect(jsonPath("$[1].quantity").value(1))
                .andExpect(jsonPath("$[1].price").value(
                        product1.getPrice().doubleValue()));
    }

    @Test
    void purchaseTest() throws Exception {
        fillBucket();

        mockMvc.perform(get("/api/v1/purchase")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/me")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance")
                        .value(new BigDecimal("400.00").doubleValue()));

        mockMvc.perform(get("/api/v1/me/bucket")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    private void fillBucket() throws Exception {
        addProductToBucket(product2.getId());
        addProductToBucket(product2.getId());
        addProductToBucket(product1.getId());
    }


    void addProductToBucket(Long productId) throws Exception {
        mockMvc.perform(post("/api/v1/bucket/products")
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(productId)))
                .andExpect(status().isOk());
    }
}
