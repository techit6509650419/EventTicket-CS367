package com.eventticket.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotFaqRequest {
    private String query;
    private Long userId;
    private String sessionId;
    private String eventId;
}