package com.eddieahiati.ecommerce.dtos;

import lombok.Data;

@Data
public class UserMessage {
    String requesterName;
    String requesterEmail;
    String message;
}
