package com.eventticket.organizer.dto.response;

import lombok.Data;
import java.util.Map;

@Data
public class TicketStatisticsResponse {
    private String eventId;
    private String eventName;
    private int totalTickets;
    private int availableTickets;
    private int reservedTickets;
    private int soldTickets;
    private int checkedInTickets;
    private int cancelledTickets;
    private double totalRevenue;
    private double potentialRevenue;
    private Map<String, TypeStatistics> statisticsByType;

    @Data
    public static class TypeStatistics {
        private String type;
        private int total;
        private int available;
        private int reserved;
        private int sold;
        private int checkedIn;
        private int cancelled;
        private double price;
        private double revenue;
        private double potentialRevenue;
    }
}