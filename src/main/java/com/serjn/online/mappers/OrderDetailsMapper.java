package com.serjn.online.mappers;

import com.serjn.online.model.DTOs.OrderDetailsDto;
import com.serjn.online.model.entities.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderDetailsMapper {

    OrderDetailsMapper INSTANCE = Mappers.getMapper(OrderDetailsMapper.class);

    OrderDetailsDto toDto(OrderDetails orderDetails);

    List<OrderDetailsDto> toDtoList(List<OrderDetails> orderDetails);
}
