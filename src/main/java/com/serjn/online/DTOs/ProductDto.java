package com.serjn.online.DTOs;


import com.serjn.online.models.Category;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @Size(min = 10,max = 40)
    private String name;
    @Size(min = 10,max = 400)

    private String description;

    private int price;

    private Category category;


}
