package com.serjn.online.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ClientDto {
    private String mail;
    private String address;
    private BigDecimal balance;

}
