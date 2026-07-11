package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.TicketAnalysis;
import com.eddieahiati.ecommerce.dtos.UserMessage;
import com.eddieahiati.ecommerce.services.AIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
@AllArgsConstructor
public class SupportController {
    private AIService aiService;

    @PostMapping
    public ResponseEntity<TicketAnalysis> aiResponse(@RequestBody UserMessage userMessage) {
        var response = aiService.analyzeSupportTicket(userMessage.getMessage());

        response.setRequesterEmail(userMessage.getRequesterEmail());
        response.setRequesterName(userMessage.getRequesterName());
        response.setMessage(userMessage.getMessage());

        return ResponseEntity.ok(response);
    }
}
