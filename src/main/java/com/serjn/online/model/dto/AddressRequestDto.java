package com.serjn.online.model.dto;

import jakarta.validation.constraints.Size;

public record AddressRequestDto(
    @Size(min = 10) String address
) {}
