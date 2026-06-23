package com.serjn.online.model.dto;

import java.math.BigDecimal;

public record ClientDto(
    String mail,
    String address,
    BigDecimal balance
) {}
