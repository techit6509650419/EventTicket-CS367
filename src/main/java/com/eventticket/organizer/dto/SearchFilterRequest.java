package com.eventticket.organizer.dto;

import com.eventticket.organizer.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterRequest {
    
    private String keyword;
    
    private String category;
    
    private Long venueId;
    
    private String venueName;
    
    private Long artistId;
    
    private String artistName;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;
    
    private Event.EventStatus status;
    
    private Long organizerId;
    
    private String organizerName;
    
    private Integer minPrice;
    
    private Integer maxPrice;
    
    private String city;
    
    private String country;
    
    private Integer page = 0;
    
    private Integer size = 10;
    
    private String sortBy = "date";
    
    private String sortDirection = "asc";
}