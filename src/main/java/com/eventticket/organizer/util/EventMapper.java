package com.eventticket.organizer.util;

import com.eventticket.organizer.dto.EventDTO;
import com.eventticket.organizer.dto.TicketTypeDTO;
import com.eventticket.organizer.dto.response.EventResponse;
import com.eventticket.organizer.dto.response.StatusUpdateResponse;
import com.eventticket.organizer.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    public EventDTO toDto(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setVenueId(event.getVenue().getId());
        dto.setVenueName(event.getVenue().getName());
        dto.setDate(event.getDate());
        dto.setTime(event.getTime());
        dto.setDuration(event.getDuration());
        dto.setOrganizerId(event.getOrganizer().getId());
        dto.setOrganizerName(event.getOrganizer().getName());
        dto.setCategory(event.getCategory());
        dto.setStatus(event.getStatus());
        dto.setImageUrl(event.getImageUrl());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());

        // Map artist IDs and names
        dto.setArtistIds(event.getArtists().stream()
                .map(Artist::getId)
                .collect(Collectors.toSet()));
        
        dto.setArtistNames(event.getArtists().stream()
                .map(Artist::getName)
                .collect(Collectors.toSet()));

        // Map ticket types
        dto.setTicketTypes(event.getTicketTypes().stream()
                .map(this::toTicketTypeDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    public EventResponse toEventResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setDate(event.getDate());
        response.setTime(event.getTime());
        response.setDuration(event.getDuration());
        response.setCategory(event.getCategory());
        response.setStatus(event.getStatus());
        response.setImageUrl(event.getImageUrl());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Map venue
        EventResponse.VenueInfo venueInfo = new EventResponse.VenueInfo(
                event.getVenue().getId(),
                event.getVenue().getName(),
                event.getVenue().getAddress(),
                event.getVenue().getCity(),
                event.getVenue().getCountry(),
                event.getVenue().getCapacity()
        );
        response.setVenue(venueInfo);

        // Map organizer
        EventResponse.OrganizerInfo organizerInfo = new EventResponse.OrganizerInfo(
                event.getOrganizer().getId(),
                event.getOrganizer().getName(),
                event.getOrganizer().getContactEmail(),
                event.getOrganizer().getWebsiteUrl()
        );
        response.setOrganizer(organizerInfo);

        // Map artists
        List<EventResponse.ArtistInfo> artistInfos = event.getArtists().stream()
                .map(artist -> new EventResponse.ArtistInfo(
                        artist.getId(),
                        artist.getName(),
                        artist.getGenre(),
                        artist.getImageUrl()
                ))
                .collect(Collectors.toList());
        response.setArtists(artistInfos);

        // Map ticket types
        response.setTicketTypes(event.getTicketTypes().stream()
                .map(this::toTicketTypeDTO)
                .collect(Collectors.toList()));

        return response;
    }

    public StatusUpdateResponse toStatusUpdateResponse(Event event) {
        StatusUpdateResponse response = new StatusUpdateResponse();
        response.setEventId(event.getId());
        response.setEventName(event.getName());
        response.setCurrentStatus(event.getStatus());

        // Get last update time
        if (!event.getStatusUpdates().isEmpty()) {
            event.getStatusUpdates().sort((u1, u2) -> u2.getTimestamp().compareTo(u1.getTimestamp()));
            response.setLastUpdated(event.getStatusUpdates().get(0).getTimestamp());
        } else {
            response.setLastUpdated(event.getUpdatedAt());
        }

        // Map status changes
        List<StatusUpdateResponse.StatusChange> changes = event.getStatusUpdates().stream()
                .map(update -> {
                    StatusUpdateResponse.StatusChange change = new StatusUpdateResponse.StatusChange();
                    change.setTimestamp(update.getTimestamp());
                    change.setField(update.getField());
                    change.setOldValue(update.getOldValue());
                    change.setNewValue(update.getNewValue());
                    change.setReason(update.getReason());
                    change.setNotificationStatus(update.getNotificationStatus());
                    return change;
                })
                .collect(Collectors.toList());
        response.setChanges(changes);

        // Map notification status from the most recent update
        if (!event.getStatusUpdates().isEmpty()) {
            EventStatusUpdate latestUpdate = event.getStatusUpdates().get(0);
            Map<String, Boolean> notificationStatus = new HashMap<>();
            
            if (latestUpdate.getNotifications() != null && !latestUpdate.getNotifications().isEmpty()) {
                notificationStatus = latestUpdate.getNotifications();
            } else {
                // Default values if notifications map is empty
                notificationStatus.put("email", false);
                notificationStatus.put("sms", false);
                notificationStatus.put("push", false);
            }
            
            response.setNotificationsStatus(notificationStatus);
        }

        return response;
    }

    private TicketTypeDTO toTicketTypeDTO(TicketType ticketType) {
        TicketTypeDTO dto = new TicketTypeDTO();
        dto.setId(ticketType.getId());
        dto.setType(ticketType.getType());
        dto.setPrice(ticketType.getPrice());
        dto.setQuantity(ticketType.getQuantity());
        dto.setMaxPerPurchase(ticketType.getMaxPerPurchase());
        dto.setSaleStartTime(ticketType.getSaleStartTime());
        dto.setSaleEndTime(ticketType.getSaleEndTime());
        dto.setBenefits(ticketType.getBenefits());
        return dto;
    }
}