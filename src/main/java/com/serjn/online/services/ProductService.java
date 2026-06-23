package com.serjn.online.services;


import com.serjn.online.exceptions.NoSuchProductException;
import com.serjn.online.model.enums.Category;
import com.serjn.online.model.entities.Product;
import com.serjn.online.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findProductsByCategory(category);
    }


    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()
                -> new NoSuchProductException("No product with id: ".concat(String.valueOf(id))));
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }


}
