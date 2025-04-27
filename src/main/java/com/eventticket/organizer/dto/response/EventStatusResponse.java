package com.eventticket.organizer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusResponse {
    
    private Long eventId;
    
    private String eventName;
    
    private String currentStatus;
    
    private LocalDateTime lastUpdated;
    
    private Boolean isBookable;
    
    private String statusMessage;
}
