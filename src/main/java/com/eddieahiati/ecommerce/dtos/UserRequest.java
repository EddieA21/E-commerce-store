package com.eddieahiati.ecommerce.dtos;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String name;
    private String email;
}
