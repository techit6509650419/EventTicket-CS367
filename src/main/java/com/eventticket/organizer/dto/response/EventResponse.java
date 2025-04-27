package com.eventticket.organizer.dto.response;

import com.eventticket.organizer.dto.TicketTypeDTO;
import com.eventticket.organizer.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private VenueInfo venue;
    
    private LocalDate date;
    
    private String time;
    
    private Integer duration;
    
    private OrganizerInfo organizer;
    
    private String category;
    
    private List<ArtistInfo> artists = new ArrayList<>();
    
    private Event.EventStatus status;
    
    private String imageUrl;
    
    private List<TicketTypeDTO> ticketTypes = new ArrayList<>();
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueInfo {
        private Long id;
        private String name;
        private String address;
        private String city;
        private String country;
        private Integer capacity;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizerInfo {
        private Long id;
        private String name;
        private String contactEmail;
        private String websiteUrl;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistInfo {
        private Long id;
        private String name;
        private String genre;
        private String imageUrl;
    }
}