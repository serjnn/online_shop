package com.serjn.online.model.dto;

import jakarta.validation.constraints.NotNull;

public record AddProductRequestDto(
    @NotNull Long productId
) {}
