package com.serjn.online.mappers;

import com.serjn.online.model.dto.OrderDetailsDto;
import com.serjn.online.model.entities.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {

    @Mapping(source = "createdAt", target = "orderDate")
    @Mapping(source = "sum", target = "totalAmount")
    @Mapping(target = "status", constant = "COMPLETED")
    OrderDetailsDto toDto(OrderDetails orderDetails);

    List<OrderDetailsDto> toDtoList(List<OrderDetails> orderDetails);
}
