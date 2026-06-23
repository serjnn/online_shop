package com.serjn.online;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serjn.online.model.dto.AuthRequestDto;
import com.serjn.online.model.dto.RegisterRequestDto;
import com.serjn.online.model.enums.Category;
import com.serjn.online.model.entities.Client;
import com.serjn.online.model.entities.Product;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

class UserControllerITest {

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
        Client client = new Client();
        client.setMail("test@example.com");
        client.setPassword("encodedPassword12345");
        RegisterRequestDto registerRequest = new RegisterRequestDto("test@example.com", "password12345");
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
        AuthRequestDto authRequest = new AuthRequestDto("test@example.com", "password12345");

        MvcResult result = mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        this.token = result.getResponse().getContentAsString();

        product1 = productRepository.save(new Product("Laptop", "Desc", new BigDecimal("1200.00"), Category.ELECTRONICS));
        product2 = productRepository.save(new Product("Toy", "Desc", new BigDecimal("25.00"), Category.TOYS));
    }

    @Test
    void shouldAddProductToBucket() throws Exception {

        mockMvc.perform(post("/api/v1/bucket/products")
                        .header("Authorization", "Bearer " + this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(product1.getId())))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldMatchAddedProducts() throws Exception {
        mockMvc.perform(post("/api/v1/bucket/products")
                .header("Authorization", "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(product2.getId())));

        mockMvc.perform(get("/api/v1/me/bucket")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product2.getId()))
                .andExpect(jsonPath("$[0].productName").value(product2.getName()))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].price").value(product2.getPrice().doubleValue()));
    }
}
