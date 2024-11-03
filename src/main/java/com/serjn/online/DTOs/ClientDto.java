package com.serjn.online.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientDto {
    private Long id;
    private String mail;
    private String address;
    private Integer balance;

}
