package com.serjn.online.controllers;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
import com.serjn.online.models.OrderDetails;
import com.serjn.online.models.Product;
import com.serjn.online.repositories.BucketRepository;
import com.serjn.online.repositories.OrderDetailsRepository;
import com.serjn.online.sevices.BucketService;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import com.serjn.online.sevices.ProductService;
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
    OrderDetailsService orderDetailsService;


    @Autowired
    ClientService clientService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {

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
                bucket, role.toLowerCase());
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
    public String adminpage(Model model) {
        model.addAttribute("people", clientService.findAll());

        return "managment/adminpage";
    }



    @GetMapping("bucket")
    public String bucket(Model model) {
        Bucket bucket = clientService.findCurrentClient().getBucket();
        model.addAttribute(bucket);


        return "products/bucket";

    }

    @GetMapping("/order")
    public String order(Model model) {

        Client curClient = clientService.findCurrentClient();
        Product[] prods = clientService.getNoramlCart(curClient);
        model.addAttribute("sum",
                Arrays.stream(prods).mapToInt(Product::getPrice).sum());
        model.addAttribute("prods", prods);
        model.addAttribute("client", curClient);
        return "user/order";
    }

    @PostMapping("/buy")
    public String buy(){
        Client client = clientService.findCurrentClient();
        Product[] prods = clientService.getNoramlCart(client);
        int sum = Arrays.stream(prods).mapToInt(Product::getPrice).sum();

        if (client.getAddress() != null  && client.getBalance() >= sum )
        {
            OrderDetails orderDetails = new OrderDetails(
                    client.getId(),
                    client.getCart().substring(1,
                            client.getCart().length()-1), // что то не так тут точно
                    sum
            );
            orderDetailsService.saveOrder(orderDetails);
            client.setBalance(client.getBalance() - sum);
            client.setCart("-");
            clientService.save(client);
            return "redirect:/orderdetails"; // перенести эту логику в сервис
        }
        else {return "redirect:/";} // понять как возвращать ошибки и вообще
        // разораться в них

    }
    @GetMapping("/orderdetails")
    public  String orderdetails(Model model){
        Client client = clientService.findCurrentClient();
        model.addAttribute("order",
                orderDetailsService.findByClientId(client.getId()));
        return "user/orderdetails";
    }

    @PostMapping("/address")
    public String address(@RequestParam String address){
        Client client = clientService.findCurrentClient()   ;
        client.setAddress( address);
        clientService.save(client);
        return "redirect:/order";
    }


}
