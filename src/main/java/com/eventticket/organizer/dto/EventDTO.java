package com.eventticket.organizer.dto;

import com.eventticket.organizer.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventDTO {

    private Long id;
    private String name;
    private String description;
    private Long venueId;
    private String venueName;
    private LocalDate date;
    private String time;
    private Integer duration;
    private Long organizerId;
    private String organizerName;
    private String category;
    private Set<Long> artistIds = new HashSet<>();
    private Set<String> artistNames = new HashSet<>();
    private Event.EventStatus status = Event.EventStatus.DRAFT;
    private String imageUrl;
    private List<TicketTypeDTO> ticketTypes = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EventDTO() {}

    public EventDTO(Long id, String name, String description, Long venueId, String venueName, LocalDate date, String time,
                    Integer duration, Long organizerId, String organizerName, String category, Set<Long> artistIds,
                    Set<String> artistNames, Event.EventStatus status, String imageUrl, List<TicketTypeDTO> ticketTypes,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.venueId = venueId;
        this.venueName = venueName;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.category = category;
        this.artistIds = artistIds;
        this.artistNames = artistNames;
        this.status = status;
        this.imageUrl = imageUrl;
        this.ticketTypes = ticketTypes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Set<Long> getArtistIds() { return artistIds; }
    public void setArtistIds(Set<Long> artistIds) { this.artistIds = artistIds; }

    public Set<String> getArtistNames() { return artistNames; }
    public void setArtistNames(Set<String> artistNames) { this.artistNames = artistNames; }

    public Event.EventStatus getStatus() { return status; }
    public void setStatus(Event.EventStatus status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<TicketTypeDTO> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketTypeDTO> ticketTypes) { this.ticketTypes = ticketTypes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}