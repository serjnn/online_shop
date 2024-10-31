package com.serjn.online.DTOs;


import lombok.Getter;

@Getter
public class RegRequest {
    private String mail;
    private String password;
    private String role;
}
