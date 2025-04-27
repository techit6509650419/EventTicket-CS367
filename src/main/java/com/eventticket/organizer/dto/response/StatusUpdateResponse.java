package com.eventticket.organizer.dto.response;

import com.eventticket.organizer.model.Event;
import com.eventticket.organizer.model.EventStatusUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateResponse {
    
    private Long eventId;
    
    private String eventName;
    
    private Event.EventStatus currentStatus;
    
    private LocalDateTime lastUpdated;
    
    private List<StatusChange> changes = new ArrayList<>();
    
    private Map<String, Boolean> notificationsStatus = new HashMap<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusChange {
        private LocalDateTime timestamp;
        private String field;
        private String oldValue;
        private String newValue;
        private String reason;
        private EventStatusUpdate.NotificationStatus notificationStatus;
    }
}