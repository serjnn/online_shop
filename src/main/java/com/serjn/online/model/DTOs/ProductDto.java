package com.serjn.online.model.DTOs;

import com.serjn.online.model.enums.Category;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductDto(
    Long id,
    @Size(min = 10, max = 40) String name,
    @Size(min = 10, max = 400) String description,
    BigDecimal price,
    Category category
) {}
