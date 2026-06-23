package com.serjn.online.mappers;

import com.serjn.online.model.dto.ProductDto;
import com.serjn.online.model.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> products);

}
