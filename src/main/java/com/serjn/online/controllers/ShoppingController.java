package com.serjn.online.controllers;

import com.serjn.online.DTOs.ProductDto;
import com.serjn.online.model.Category;
import com.serjn.online.model.Product;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.ProductService;
import com.serjn.online.sevices.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShoppingController {

    private final BucketService bucketService;
    private final PurchaseService purchaseService;
    private final ProductService productService;

    @GetMapping("/purchase")
    void purchase() {
        purchaseService.purchase();
    }

    @PostMapping("/bucket")
    void addProductToBucket(@RequestBody Long productId) {
        bucketService.addProductToBucket(productId);
    }

    @DeleteMapping("/bucket/{productId}")
    void removeFromBucket(@PathVariable("productId") Long productId) {
        bucketService.removeProductFromBucket(productId);
    }

//TODO return dtos
    @GetMapping("/products")
    List<Product> findProductsByCategory(@RequestParam("category") Category category) {
        return productService.findProductsByCategory(category);
    }

    @PostMapping("/products")
    void addNewProduct(@RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);

    }
}
