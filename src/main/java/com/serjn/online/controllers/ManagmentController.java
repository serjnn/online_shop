package com.serjn.online.controllers;


import com.serjn.online.models.Category;
import com.serjn.online.models.Product;
import com.serjn.online.sevices.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManagmentController {

    @Autowired
    ProductService productService;

    @GetMapping("/managerpage")
    public String managerpage(Model model){
        model.addAttribute("categories", Category.values());

        return "managment/managerpage";
    }

    @PostMapping("/addprod")
    public String addprod(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam Integer price,
                          @RequestParam Category category){

        productService.save(new Product(name,description,price,category));
        return "redirect:/categories";
    }
}
