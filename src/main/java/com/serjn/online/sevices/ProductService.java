package com.serjn.online.sevices;


import com.serjn.online.exceptions.NoSuchProductException;
import com.serjn.online.model.enums.Category;
import com.serjn.online.model.entities.Product;
import com.serjn.online.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findProductsByCategory(category);
    }


    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()
                -> new NoSuchProductException("No product with id: ".concat(String.valueOf(id))));
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }


}
