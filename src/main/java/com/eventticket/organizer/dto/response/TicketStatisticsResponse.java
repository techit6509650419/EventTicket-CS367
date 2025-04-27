package com.eventticket.organizer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatisticsResponse {
    
    private Long eventId;
    
    private String eventName;
    
    private int totalTickets;
    
    private int soldTickets;
    
    private int availableTickets;
    
    private Map<String, TypeStats> ticketTypeStats = new HashMap<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeStats {
        private String type;
        private int total;
        private int sold;
        private int available;
        private double percentageSold;
    }
}