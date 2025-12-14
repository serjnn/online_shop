package com.serjn.online.sevices;


import com.serjn.online.model.Category;
import com.serjn.online.model.Product;
import com.serjn.online.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findProductsByCategory(category);
    }


    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("No product with id: ".concat(String.valueOf(id))));
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }


}
