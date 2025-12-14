package com.serjn.online;

import com.serjn.online.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @RequiredArgsConstructor
public class OnlineApplication  {

    private final ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(OnlineApplication.class, args);
    }

//    @Override public void run(ApplicationArguments args) throws Exception {
//
//        Product product1 = new Product("Laptop", "Powerful laptop for work and gaming", 1200, Category.ELECTRONICS);
//        Product product2 = new Product("Smartphone", "Latest model smartphone with advanced features", 800, Category.ELECTRONICS);
//        Product product3 = new Product("T-Shirt", "Comfortable cotton t-shirt", 25, Category.TOYS);
//        Product product4 = new Product("Jeans", "Stylish denim jeans", 60, Category.FOOD);
//        Product product5 = new Product("Book", "Bestselling novel", 15, Category.CLOTH);
//
//        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));
    }

