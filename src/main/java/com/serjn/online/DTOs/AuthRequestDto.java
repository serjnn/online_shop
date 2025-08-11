package com.serjn.online.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthRequestDto {

    @Email
    @NotBlank
    private String mail;

    @NotBlank
    @Size(min = 10, max = 40)
    private String password;
}
