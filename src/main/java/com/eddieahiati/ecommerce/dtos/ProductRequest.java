package com.eddieahiati.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private BigDecimal price = BigDecimal.ZERO;
}
