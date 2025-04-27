package com.eventticket.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRequest {
    
    private String name;
    
    private String description;
    
    private Long venueId;
    
    private LocalDate date;
    
    private String time;
    
    @Positive(message = "Duration must be positive")
    private Integer duration;
    
    private String category;
    
    private Set<Long> artistIds = new HashSet<>();
    
    private String imageUrl;
    
    @Valid
    private List<TicketTypeDTO> ticketTypes = new ArrayList<>();
    
    private String updateReason;
}