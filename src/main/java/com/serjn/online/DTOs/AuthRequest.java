package com.serjn.online.DTOs;


import lombok.Data;

@Data
public class AuthRequest {
    private String mail;
    private String password;
}
