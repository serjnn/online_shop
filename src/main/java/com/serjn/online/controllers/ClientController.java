package com.serjn.online.controllers;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.BucketItems;
import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientService;
import com.serjn.online.sevices.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

        Client client = new Client(mail,
                passwordEncoder.encode(password),
                bucket,
                role.toLowerCase());
        bucket.setClient(client);
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
        Client client = clientService.findCurrentClient();
        List<BucketItems> bitems = clientService.getBItemsListOfClient(client);
        int sum = bitems.stream().mapToInt(i -> i.getProduct().getPrice() *
                i.getQuantity()).sum();
        model.addAttribute("bitems", bitems);
        model.addAttribute("sum", sum);
        model.addAttribute("client", client);
        return "user/order";
    }

    @PostMapping("/buy")
    public String buy() {
        return clientService.buy(clientService.findCurrentClient()) ? "redirect:/orderdetails" : "redirect:/failture";


    }

    @GetMapping("/orderdetails")
    public String orderdetails(Model model) {
        Client client = clientService.findCurrentClient();
        model.addAttribute("orders",
                orderDetailsService.findByClientId(client.getId()));
        return "user/orderdetails";
    }

    @PostMapping("/address")
    public String address(@RequestParam String address) {
        clientService.setAddress(address);
        return "redirect:/order";
    }

    @GetMapping("/failture")
    public String failture() {
        return "user/failture";
    }

    @PostMapping("deleteOrders")
    public String deleteOrders() {
        orderDetailsService.deleteALl();
        return "redirect:/orderdetails";
    }

}
