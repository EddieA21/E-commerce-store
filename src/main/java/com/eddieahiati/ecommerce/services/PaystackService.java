package com.eddieahiati.ecommerce.services;

import com.eddieahiati.ecommerce.entities.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PaystackService {

    public String initializePayment(Order order) throws Exception {
        // Get customer email from order user
        String jsonBody = getJsonBody(order);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.paystack.co/transaction/initialize"))
                .header("Authorization", "Bearer " + System.getenv("PAYSTACK_SECRET"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send request and parse response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Extract authorization URL from response
        // The response format: {"status":true,"data":{"authorization_url":"https://..."}}
        return extractAuthUrl(response.body());
    }

    private static String getJsonBody(Order order) {
        String email = order.getUser().getEmail();

        // Convert price to pesewas (smallest currency unit)
        int amountInPesewas = order.getTotalPrice().multiply(BigDecimal.valueOf(100)).intValue();

        String reference = order.getTransactionReference();

        // Build JSON string with order values
        String jsonBody = String.format("""
            {
                "email": "%s",
                "amount": %d,
                "currency": "GHS",
                "reference": "%s"
            }
            """, email, amountInPesewas, reference);
        return jsonBody;
    }

    private String extractAuthUrl(String response) {
        // Simple parsing (use JSON library like Jackson in real code)
        // Returns the authorization_url for frontend to redirect to
        return response.split("authorization_url\":\"")[1].split("\"")[0];
    }
}
