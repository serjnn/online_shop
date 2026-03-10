package com.serjn.online.model.DTOs;

import java.math.BigDecimal;

public record ClientDto(
    String mail,
    String address,
    BigDecimal balance
) {}
