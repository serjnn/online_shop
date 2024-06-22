package com.serjn.online.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String mail;
    private String password;
    private String address;

    @OneToOne(mappedBy ="client", cascade = CascadeType.ALL)
    private Bucket bucket;

    private String role;

    public Client(String mail, String password, String role){
        this.mail = mail;
        this.password = password;
        this.role = role;
    }
}
