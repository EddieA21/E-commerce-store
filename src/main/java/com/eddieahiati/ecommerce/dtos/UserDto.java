package com.eddieahiati.ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Name shouldn't be blank")
    private String name;
    @Email(message = "Should be a valid email")
    private String email;
    private String password;
}
