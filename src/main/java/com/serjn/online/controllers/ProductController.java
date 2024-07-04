package com.serjn.online.controllers;

import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Category;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.BucketItemsService;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    BucketService bucketService;
    @Autowired
    ProductService productService;

    @Autowired
    ClientService clientService;

    @Autowired
    BucketItemsService bucketItemsService;


    @GetMapping("/categories")
    public String categories(Model model) {

        model.addAttribute("cats", Category.values());
        model.addAttribute("prods", productService.findAll());
        return "products/categories";
    }

    @GetMapping("/products")
    public String productsByCat(@RequestParam Category category, Model model) {
        model.addAttribute("products",
                productService.getProductsByCategory(category));
        return "products/productsByCat";

    }

    @Transactional
    @PostMapping("addToCart")
    public String addToCart(@RequestParam Long productId) {
        Client client = clientService.findCurrentClient();
        Bucket bucket = bucketService.findBucketByClientId(client.getId());

        Optional<BucketItems> existingBucketItem =bucket.getBucketItems()
                .stream()
                .filter(bucketItem -> bucketItem.getProduct().getId() == productId)
                .findFirst();

        if (existingBucketItem.isPresent()) {
            BucketItems bucketItems = bucketItemsService.findBucketItemByProductId(productId);
            bucketItems.setQuantity(bucketItems.getQuantity() + 1);
            bucketService.save(bucket);
            return "redirect:categories";
        }
        else {
            BucketItems bucketItems = new BucketItems(
                    productService.findById(productId),
                    bucket,
                    1);

            bucket.getBucketItems().add(bucketItems);
            bucketService.save(bucket);
            return "redirect:categories";

        }





    }


}
