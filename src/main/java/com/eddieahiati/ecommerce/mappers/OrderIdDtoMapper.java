package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.OrderIdDto;
import com.eddieahiati.ecommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderIdDtoMapper {
    @Mapping(target = "orderId", source = "id")
    OrderIdDto getOrderIdDto(Order order);
}
