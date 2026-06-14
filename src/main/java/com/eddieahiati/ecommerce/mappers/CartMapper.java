package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.CartDto;
import com.eddieahiati.ecommerce.dtos.CartRequest;
import com.eddieahiati.ecommerce.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto cartDto(Cart cart);
    @Mapping(target = "totalPrice", expression = "java(cart.overAllTotal())")
    CartRequest cartRequest(Cart cart);
}
