package com.serjn.online.mappers;

import com.serjn.online.model.dto.BucketItemDto;
import com.serjn.online.model.entities.BucketItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BucketItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    BucketItemDto toDto(BucketItem bucketItem);

    List<BucketItemDto> toDtoList(List<BucketItem> bucketItems);
}
