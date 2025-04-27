package com.eventticket.organizer.dto.response;

import com.eventticket.organizer.dto.SearchFilterRequest;
import com.eventticket.organizer.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    
    private List<EventSummary> results = new ArrayList<>();
    
    private int totalResults;
    
    private int page;
    
    private int size;
    
    private int totalPages;
    
    private SearchFilterRequest searchParams;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventSummary {
        private Long id;
        private String name;
        private LocalDate date;
        private String time;
        private String venue;
        private String category;
        private List<String> artists = new ArrayList<>();
        private Event.EventStatus status;
        private TicketPriceRange ticketPrice;
        private String imageUrl;
        private String availability;
        private Integer ticketsLeft;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketPriceRange {
        private BigDecimal min;
        private BigDecimal max;
    }
}