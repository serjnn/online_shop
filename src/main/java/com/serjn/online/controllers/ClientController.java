package com.serjn.online.controllers;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.BucketRepository;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class ClientController {
    @Autowired
    BucketService bucketService;

    @Autowired
    ClientService clientService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model)
    {

        Client curClient = clientService.findCurrentClient();
        model.addAttribute("client", curClient);
        model.addAttribute("role", curClient.getRole().equals("manager") ?
                "менеджера" : curClient.getRole().equals("admin") ? "админа" : "клиента");

        return "main/home";
    }
    @GetMapping("/register")
    public String registerMap() {

        return "user/register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String mail,
                               @RequestParam String password,
                               @RequestParam String role) {
        Bucket bucket = new Bucket();

        Client client = new Client(mail, passwordEncoder.encode(password),
                bucket,role.toLowerCase());
        bucket.setClient(client);
//        bucketService.save(bucket);
        clientService.save(client);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "user/myLogin";
    }
    @GetMapping("/adminpage")
    public String adminpage( Model model){
        model.addAttribute("people", clientService.findAll());

        return "managment/adminpage";
    }
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long user_id){
        clientService.deleteById(user_id);
        return  "redirect:/";
    }

    @GetMapping("bucket")
    public String bucket(Model model){
        Bucket bucket = clientService.findCurrentClient().getBucket();
        model.addAttribute(bucket);


        return "products/bucket";

    }

    @GetMapping("/order")
    public String order(Model model){

        Client curClient = clientService.findCurrentClient();
        Product [] prods = clientService.getNoramlCart(curClient);
        model.addAttribute("sum",
                Arrays.stream(prods).mapToInt(Product::getPrice).sum());
        model.addAttribute("prods", prods   );
        model.addAttribute("client", curClient);
        return "user/order";
    }


}
