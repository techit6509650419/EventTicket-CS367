package com.eventticket.organizer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long bookingId;
    private Long userId;
    private String eventId;
    private String eventName;
    private List<Ticket> tickets;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private Map<String, Link> _links;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ticket {
        private Long id;
        private String type;
        private String section;
        private String seatNumber;
        private double price;
        private String status;
        private String qrCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private String href;
        private String hreflang;
        private String title;
        private String type;
        private String deprecation;
        private String profile;
        private String name;
        private boolean templated;
    }
}