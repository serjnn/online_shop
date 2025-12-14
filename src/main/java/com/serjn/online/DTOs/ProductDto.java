package com.serjn.online.DTOs;


import com.serjn.online.model.Category;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class ProductDto {

    @Size(min = 10,max = 40)
    private String name;

    @Size(min = 10,max = 400)
    private String description;

    private BigDecimal price;

    private Category category;


}
