package com.eddieahiati.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCartDto {
    private ProductRequest product;
    private Integer quantity;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
