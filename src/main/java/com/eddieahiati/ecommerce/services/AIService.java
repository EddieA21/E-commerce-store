package com.eddieahiati.ecommerce.services;

import com.eddieahiati.ecommerce.dtos.TicketAnalysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TicketAnalysis analyzeSupportTicket(String customerMessage) {
        try {
            String url = "https://openrouter.ai/api/v1/chat/completions";

            String prompt = buildPrompt(customerMessage);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek/deepseek-chat");
            body.put("messages", List.of(
                    Map.of("role", "user", "content", prompt)
            ));
            body.put("temperature", 0.3);
            body.put("max_tokens", 800);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            String jsonResponse = extractTextFromResponse(response.getBody());
            System.out.println("Raw AI Response: " + jsonResponse);

            return parseJsonToTicketAnalysis(jsonResponse);

        } catch (Exception e) {
            return TicketAnalysis.errorResponse("Error: " + e.getMessage());
        }
    }

    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Error extracting response: " + e.getMessage();
        }
    }

    private String buildPrompt(String customerMessage) {
        return String.format("""
            You are an AI support analyst for an e-commerce platform.
            
            Analyze this customer support ticket and return ONLY valid JSON with ALL these fields:
            
            {
              "summary": "One sentence summarizing the issue",
              "category": "BILLING or TECHNICAL or LOGIN or FEATURE_REQUEST or COMPLAINT or OTHER",
              "priority": "LOW or MEDIUM or HIGH or CRITICAL",
              "sentiment": "POSITIVE or NEUTRAL or NEGATIVE or FRUSTRATED",
              "suggestedResponse": "A specific, helpful reply to send to the customer. Address their exact issue. Include actionable steps. (max 100 words)",
              "internalNotes": "What the support team should check. Include specific systems, tools, or data to verify. (max 50 words)",
              "requiresEscalation": true or false,
              "confidenceScore": 0.0 to 1.0
            }
            
            IMPORTANT RULES:
            1. Category: BILLING (payments/refunds/subscriptions), TECHNICAL (bugs/crashes), LOGIN (password/access), FEATURE_REQUEST (suggestions), COMPLAINT (angry customers)
            2. Priority: CRITICAL (system down/data loss), HIGH (payments blocked/can't login), MEDIUM (bugs/questions), LOW (feedback/docs)
            3. suggestedResponse MUST be a complete, helpful reply that the customer would understand
            4. internalNotes MUST contain specific actions for the support team (e.g., "Check Stripe invoice #123", "Verify webhook was received")
            5. Return ONLY valid JSON. No markdown, no explanations, just the JSON.
            
            Customer Message: %s
            """, customerMessage);
    }

    private TicketAnalysis parseJsonToTicketAnalysis(String json) {
        try {
            json = json.replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();

            int start = json.indexOf("{");
            int end = json.lastIndexOf("}");
            if (start >= 0 && end > start) {
                json = json.substring(start, end + 1);
            }

            return objectMapper.readValue(json, TicketAnalysis.class);

        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + json);
            TicketAnalysis error = TicketAnalysis.errorResponse("Failed to parse AI response: " + e.getMessage());
            error.setRawResponse(json);
            return error;
        }
    }
}