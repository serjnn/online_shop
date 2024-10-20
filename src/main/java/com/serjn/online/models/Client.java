package com.serjn.online.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 70, nullable = false)
    private String mail;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 300)
    private String address;


    @JsonIgnore
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Bucket bucket;

    @Column(length = 10, nullable = false)
    private String role;



    private int balance = 0;

    public Client(String mail, String password, Bucket bucket, String role) {
        this.mail = mail;
        this.password = password;

        this.bucket = bucket;
        this.role = role;
    }
}
