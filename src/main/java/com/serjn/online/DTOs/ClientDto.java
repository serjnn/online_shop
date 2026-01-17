package com.serjn.online.DTOs;

import java.math.BigDecimal;

public record ClientDto(
    String mail,
    String address,
    BigDecimal balance
) {}
