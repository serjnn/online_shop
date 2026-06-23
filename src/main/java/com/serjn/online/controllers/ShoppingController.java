package com.serjn.online.controllers;

import com.serjn.online.model.dto.ProductDto;
import com.serjn.online.mappers.ProductMapper;
import com.serjn.online.model.enums.Category;
import com.serjn.online.services.BucketService;
import com.serjn.online.services.ProductService;
import com.serjn.online.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.serjn.online.model.dto.AddProductRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Shopping", description = "Endpoints for shopping operations")
public class ShoppingController {

    private final BucketService bucketService;
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Purchase items", description = "Processes the purchase of items in the bucket")
    @PostMapping("/purchase")
    void purchase() {
        purchaseService.purchase();
    }

    @Operation(summary = "Add product to bucket", description = "Adds a product to the user's bucket")
    @PostMapping("/bucket/products")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    void addProductToBucket(@Valid @RequestBody AddProductRequestDto request) {
        bucketService.addProductToBucket(request.productId());
    }

    @Operation(summary = "Remove product from bucket", description = "Removes a product from the user's bucket")
    @DeleteMapping("/bucket/products/{productId}")
    void removeFromBucket(@PathVariable("productId") Long productId) {
        bucketService.removeProductFromBucket(productId);
    }

    @Operation(summary = "Find products by category", description = "Retrieves a list of products in a specific category")
    @GetMapping("/products")
    List<ProductDto> findProductsByCategory(@RequestParam("category") Category category) {
        return productMapper.toDtoList(productService.findProductsByCategory(category));
    }

    @Operation(summary = "Add new product", description = "Adds a new product to the catalog")
    @PostMapping("/products")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    void addNewProduct(@RequestBody ProductDto productDto) {
        productService.saveProduct(productMapper.toEntity(productDto));
    }
}
