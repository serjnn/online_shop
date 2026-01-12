package com.serjn.online.repositories;

import com.serjn.online.model.Category;
import com.serjn.online.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findProductsByCategory(Category category);


}
