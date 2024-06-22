package com.serjn.online.sevices;


import com.serjn.online.models.Category;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProductsByCategory(Category category){
        return productRepository.findProductsByCategory(category);
    }
    public void save(Product product){
        productRepository.save(product);
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
