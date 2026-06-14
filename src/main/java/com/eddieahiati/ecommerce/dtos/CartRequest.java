package com.eddieahiati.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartRequest {
    private UUID id;
    private List<ProductCartDto> cartItems;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
