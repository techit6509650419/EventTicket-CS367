package com.eventticket.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long eventId; // Add this field
    private Long userId;
    private String ticketType;
    private int quantity;
    private String note; // Optional field
}
