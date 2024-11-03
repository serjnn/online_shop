package com.serjn.online.controllers;


import com.serjn.online.models.Category;
import com.serjn.online.models.Product;
import com.serjn.online.sevices.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UtilitiesController {

    private final ProductService productService;


    @GetMapping("/secured")
    String secured() {
        return "";
    }

    @GetMapping("/categories/{cat}")
    List<Product> getProductsByCategory(@PathVariable("cat") Category category) {
        return productService.getProductsByCategory(category);
    }

    @PostMapping("/newProduct")
    void newProduct(@RequestBody Product product) {
         productService.save(product);

    }


}
