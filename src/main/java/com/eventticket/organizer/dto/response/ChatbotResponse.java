package com.eventticket.organizer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    private String answer;
    private double confidence;
    private List<FAQ> relatedFaq;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FAQ {
        private String id;
        private String question;
    }
}