package com.serjn.online.mappers;

import com.serjn.online.model.DTOs.BucketItemDto;
import com.serjn.online.model.entities.BucketItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BucketItemMapper {

    BucketItemMapper INSTANCE = Mappers.getMapper(BucketItemMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    BucketItemDto toDto(BucketItem bucketItem);

    List<BucketItemDto> toDtoList(List<BucketItem> bucketItems);
}
