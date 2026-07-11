package com.eddieahiati.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketAnalysis {

    private String requesterName;
    private String requesterEmail;
    private String message;
    private String summary;
    private String category;
    private String priority;
    private String sentiment;
    private String suggestedResponse;
    private String internalNotes;
    private boolean requiresEscalation = false;
    private double confidenceScore = 0.0;

    // For debugging
    private String rawResponse;

    // Error fields
    private boolean error = false;
    private String errorMessage;

    public static TicketAnalysis errorResponse(String message) {
        TicketAnalysis analysis = new TicketAnalysis();
        analysis.setError(true);
        analysis.setErrorMessage(message);
        return analysis;
    }

    // Helper methods
    public String getCustomerDisplayMessage() {
        if (suggestedResponse != null && !suggestedResponse.isEmpty()) {
            return suggestedResponse;
        }
        return "We've received your request and will get back to you shortly.";
    }

    public boolean isUrgent() {
        return "CRITICAL".equals(priority) || "HIGH".equals(priority);
    }

    public String getPriorityEmoji() {
        switch (priority) {
            case "CRITICAL": return "🚨";
            case "HIGH": return "⚠️";
            case "MEDIUM": return "📌";
            case "LOW": return "📝";
            default: return "📩";
        }
    }
}