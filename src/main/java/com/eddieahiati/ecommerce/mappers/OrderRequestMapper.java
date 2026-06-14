package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.OrderRequestDto;
import com.eddieahiati.ecommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {
    @Mapping(target = "items", source = "orderItems")
    OrderRequestDto toOrderDto(Order order);
}
