package com.eventticket.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {
    
    @NotBlank(message = "Event name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Venue ID is required")
    private Long venueId;
    
    @NotNull(message = "Event date is required")
    private LocalDate date;
    
    @NotBlank(message = "Event time is required")
    private String time;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration;
    
    @NotNull(message = "Organizer ID is required")
    private Long organizerId;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotEmpty(message = "At least one artist is required")
    private Set<Long> artistIds = new HashSet<>();
    
    private String imageUrl;
    
    @Valid
    @NotEmpty(message = "At least one ticket type is required")
    private List<TicketTypeDTO> ticketTypes = new ArrayList<>();
}