package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.ProductCartDto;
import com.eddieahiati.ecommerce.entities.CartItem;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    ProductCartDto productCartDto(CartItem cartItem);
}
