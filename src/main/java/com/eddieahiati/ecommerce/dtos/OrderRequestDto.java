package com.eddieahiati.ecommerce.dtos;

import com.eddieahiati.ecommerce.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long id;
    private OrderStatus status;
    private Date createdAt = new Date();
    private List<ProductCartDto> items;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
