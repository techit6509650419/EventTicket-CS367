package com.eventticket.organizer.service;

import com.eventticket.organizer.dto.EventCreateRequest;
import com.eventticket.organizer.dto.EventDTO;
import com.eventticket.organizer.dto.EventUpdateRequest;
import com.eventticket.organizer.dto.TicketTypeDTO;
import com.eventticket.organizer.dto.response.EventResponse;
import com.eventticket.organizer.exception.BusinessException;
import com.eventticket.organizer.exception.ResourceNotFoundException;
import com.eventticket.organizer.model.*;
import com.eventticket.organizer.repository.ArtistRepository;
import com.eventticket.organizer.repository.EventRepository;
import com.eventticket.organizer.repository.OrganizerRepository;
import com.eventticket.organizer.repository.VenueRepository;
import com.eventticket.organizer.util.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final OrganizerRepository organizerRepository;
    private final EventMapper eventMapper;

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Event event = findEventById(id);
        return eventMapper.toDto(event);
    }

    public EventResponse getEventResponse(Long id) {
        Event event = findEventById(id);
        return eventMapper.toEventResponse(event);
    }

    public List<EventDTO> getEventsByStatus(Event.EventStatus status) {
        List<Event> events = eventRepository.findByStatus(status);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByOrganizer(Long organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByVenue(Long venueId) {
        List<Event> events = eventRepository.findByVenueId(venueId);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDTO createEvent(EventCreateRequest request) {
        validateEventCreateRequest(request);
        
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + request.getVenueId()));
        
        Organizer organizer = organizerRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with id: " + request.getOrganizerId()));
        
        Set<Artist> artists = request.getArtistIds().stream()
                .map(id -> artistRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id)))
                .collect(Collectors.toSet());
        
        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setVenue(venue);
        event.setDate(request.getDate());
        event.setTime(request.getTime());
        event.setDuration(request.getDuration());
        event.setOrganizer(organizer);
        event.setCategory(request.getCategory());
        event.setArtists(artists);
        event.setStatus(Event.EventStatus.DRAFT);
        event.setImageUrl(request.getImageUrl());
        
        for (TicketTypeDTO ticketTypeDTO : request.getTicketTypes()) {
            TicketType ticketType = new TicketType();
            ticketType.setType(ticketTypeDTO.getType());
            ticketType.setPrice(ticketTypeDTO.getPrice());
            ticketType.setQuantity(ticketTypeDTO.getQuantity());
            ticketType.setMaxPerPurchase(ticketTypeDTO.getMaxPerPurchase());
            ticketType.setSaleStartTime(ticketTypeDTO.getSaleStartTime());
            ticketType.setSaleEndTime(ticketTypeDTO.getSaleEndTime());
            ticketType.setBenefits(ticketTypeDTO.getBenefits());
            
            event.addTicketType(ticketType);
        }
        
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventUpdateRequest request) {
        Event event = findEventById(id);
        
        if (request.getName() != null) {
            event.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        
        if (request.getVenueId() != null) {
            Venue venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + request.getVenueId()));
            
            String oldVenue = event.getVenue().getName();
            event.setVenue(venue);
            
            EventStatusUpdate statusUpdate = new EventStatusUpdate();
            statusUpdate.setField("venue");
            statusUpdate.setOldValue(oldVenue);
            statusUpdate.setNewValue(venue.getName());
            statusUpdate.setReason(request.getUpdateReason());
            statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
            event.addStatusUpdate(statusUpdate);
        }
        
        if (request.getDate() != null) {
            String oldDate = event.getDate().toString();
            event.setDate(request.getDate());
            
            EventStatusUpdate statusUpdate = new EventStatusUpdate();
            statusUpdate.setField("date");
            statusUpdate.setOldValue(oldDate);
            statusUpdate.setNewValue(request.getDate().toString());
            statusUpdate.setReason(request.getUpdateReason());
            statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
            event.addStatusUpdate(statusUpdate);
        }
        
        if (request.getTime() != null) {
            String oldTime = event.getTime();
            event.setTime(request.getTime());
            
            EventStatusUpdate statusUpdate = new EventStatusUpdate();
            statusUpdate.setField("time");
            statusUpdate.setOldValue(oldTime);
            statusUpdate.setNewValue(request.getTime());
            statusUpdate.setReason(request.getUpdateReason());
            statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
            event.addStatusUpdate(statusUpdate);
        }
        
        if (request.getDuration() != null) {
            event.setDuration(request.getDuration());
        }
        
        if (request.getCategory() != null) {
            event.setCategory(request.getCategory());
        }
        
        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
            Set<Artist> artists = request.getArtistIds().stream()
                    .map(artistId -> artistRepository.findById(artistId)
                            .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + artistId)))
                    .collect(Collectors.toSet());
            
            event.getArtists().clear();
            event.setArtists(artists);
        }
        
        if (request.getImageUrl() != null) {
            event.setImageUrl(request.getImageUrl());
        }
        
        if (request.getTicketTypes() != null && !request.getTicketTypes().isEmpty()) {
            updateTicketTypes(event, request.getTicketTypes());
        }
        
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }

    @Transactional
    public EventDTO publishEvent(Long id) {
        Event event = findEventById(id);
        
        if (event.getStatus() != Event.EventStatus.DRAFT) {
            throw new BusinessException("Only draft events can be published");
        }
        
        if (event.getTicketTypes().isEmpty()) {
            throw new BusinessException("Event must have at least one ticket type before publishing");
        }
        
        event.setStatus(Event.EventStatus.UPCOMING);
        
        EventStatusUpdate statusUpdate = new EventStatusUpdate();
        statusUpdate.setField("status");
        statusUpdate.setOldValue(Event.EventStatus.DRAFT.toString());
        statusUpdate.setNewValue(Event.EventStatus.UPCOMING.toString());
        statusUpdate.setReason("Event published");
        statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
        event.addStatusUpdate(statusUpdate);
        
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = findEventById(id);
        
        if (event.getStatus() != Event.EventStatus.DRAFT) {
            throw new BusinessException("Only draft events can be deleted");
        }
        
        eventRepository.delete(event);
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    private void validateEventCreateRequest(EventCreateRequest request) {
        if (request.getTicketTypes() == null || request.getTicketTypes().isEmpty()) {
            throw new BusinessException("At least one ticket type is required");
        }
        
        if (request.getArtistIds() == null || request.getArtistIds().isEmpty()) {
            throw new BusinessException("At least one artist is required");
        }
    }

    private void updateTicketTypes(Event event, List<TicketTypeDTO> ticketTypeDTOs) {
        // Remove ticket types not in the request
        event.getTicketTypes().removeIf(existingType -> 
                ticketTypeDTOs.stream()
                        .noneMatch(dto -> dto.getId() != null && dto.getId().equals(existingType.getId())));
        
        // Update existing ticket types and add new ones
        for (TicketTypeDTO dto : ticketTypeDTOs) {
            if (dto.getId() != null) {
                // Update existing ticket type
                event.getTicketTypes().stream()
                        .filter(type -> type.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(type -> {
                            type.setType(dto.getType());
                            type.setPrice(dto.getPrice());
                            type.setQuantity(dto.getQuantity());
                            type.setMaxPerPurchase(dto.getMaxPerPurchase());
                            type.setSaleStartTime(dto.getSaleStartTime());
                            type.setSaleEndTime(dto.getSaleEndTime());
                            type.setBenefits(dto.getBenefits());
                        });
            } else {
                // Add new ticket type
                TicketType newType = new TicketType();
                newType.setType(dto.getType());
                newType.setPrice(dto.getPrice());
                newType.setQuantity(dto.getQuantity());
                newType.setMaxPerPurchase(dto.getMaxPerPurchase());
                newType.setSaleStartTime(dto.getSaleStartTime());
                newType.setSaleEndTime(dto.getSaleEndTime());
                newType.setBenefits(dto.getBenefits());
                
                event.addTicketType(newType);
            }
        }
    }
}