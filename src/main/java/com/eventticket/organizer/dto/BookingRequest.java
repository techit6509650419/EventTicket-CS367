package com.eventticket.organizer.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private String eventId;
    private String ticketType;
    private Integer quantity;
    private String note;
}
