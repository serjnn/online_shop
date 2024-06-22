package com.serjn.online.controllers;


import com.serjn.online.models.Client;
import com.serjn.online.sevices.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {

    @Autowired
    ClientService clientService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerMap() {

        return "user/register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String mail,
                               @RequestParam String password,
                               @RequestParam String role) {

        Client client = new Client(mail, passwordEncoder.encode(password), role.toLowerCase());
        clientService.save(client);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "user/myLogin";
    }
    @GetMapping("/adminpage")
    public String adminpage(){
        return "user/adminpage";
    }
}
