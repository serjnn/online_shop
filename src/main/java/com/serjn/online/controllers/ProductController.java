package com.serjn.online.controllers;

import com.serjn.online.models.Category;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {


    @Autowired
    ProductService productService;


    @GetMapping("/categories")
    public String categories(Model model){

        model.addAttribute("cats", Category.values());
        model.addAttribute("prods", productService.findAll());
        return "products/categories";
    }

    @GetMapping("/products")
    public String productsByCat(@RequestParam Category category, Model model){
        model.addAttribute("products",
                productService.getProductsByCategory(category));
        return "products/productsByCat";

    }



}
