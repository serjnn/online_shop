package com.serjn.online.controllers;

import com.serjn.online.DTOs.ProductDto;
import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import com.serjn.online.models.Category;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.models.Product;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.OrderDetailsService;
import com.serjn.online.sevices.ProductService;
import com.serjn.online.sevices.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShoppingController {

    private final BucketService bucketService;
    private final OrderDetailsService orderDetailsService;
    private final PurchaseService purchaseService;
    private final ProductService productService;


    @GetMapping("/purchase")
    ResponseEntity<String> purchase() {
        try {
            purchaseService.purchase();
        } catch (EmptyAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty address");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough money");

        }
        return null;

    }

    @GetMapping("/findClientsOrderDetails/{clientId}")
    List<OrderDetails> findClientsOrderDetails() {
        return orderDetailsService.findClientsOrderDetails();
    }

    @GetMapping("/addProductToBucket/{productId}")
    void addProductToBucket(@PathVariable("productId") Long productId) {
        bucketService.addProductToBucket(productId);
    }

    @GetMapping("/removeProduct/{productId}")
    void removeFromBucket(@PathVariable("productId") Long productId) {
        bucketService.removeProductFromBucket(productId);
    }


    @GetMapping("/findProductByCat/{category}")
    List<Product> findProductByCat(@PathVariable("category") Category category) {
        return productService.findProductsByCategory(category);
    }

    @PostMapping("/addNewProduct")
    void addNewProduct(@RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);

    }
}
