package com.serjn.online.DTOs;


import lombok.Data;

@Data
public class RegRequest {
    private String mail;
    private String password;
    private String role;
}
