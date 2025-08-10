package com.serjn.online.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthRequestDto {
    @Email
    private String mail;
    @Size(min = 10, max = 40)
    private String password;
}
