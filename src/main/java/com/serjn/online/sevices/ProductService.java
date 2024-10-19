package com.serjn.online.sevices;


import com.serjn.online.models.Category;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findProductsByCategory(category);
    }



    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No product with id: " + id));
    }


}
