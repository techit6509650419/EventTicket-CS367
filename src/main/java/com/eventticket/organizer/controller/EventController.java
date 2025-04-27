package com.eventticket.organizer.controller;

import com.eventticket.organizer.dto.EventCreateRequest;
import com.eventticket.organizer.dto.EventDTO;
import com.eventticket.organizer.dto.EventUpdateRequest;
import com.eventticket.organizer.dto.response.EventResponse;
import com.eventticket.organizer.model.Event;
import com.eventticket.organizer.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event API", description = "Endpoints for managing events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Get all events", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<CollectionModel<EntityModel<EventDTO>>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        
        List<EntityModel<EventDTO>> eventResources = events.stream()
                .map(event -> EntityModel.of(event, 
                        linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel()))
                .collect(Collectors.toList());
        
        Link link = linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel();
        CollectionModel<EntityModel<EventDTO>> result = CollectionModel.of(eventResources, link);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        
        EntityModel<EventDTO> resource = EntityModel.of(event,
                linkTo(methodOn(EventController.class).getEventById(id)).withSelfRel(),
                linkTo(methodOn(EventController.class).getAllEvents()).withRel("all_events"));
        
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Get public event details by ID")
    public ResponseEntity<EventResponse> getPublicEventDetails(@PathVariable Long id) {
        EventResponse event = eventService.getEventResponse(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get events by status", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<CollectionModel<EntityModel<EventDTO>>> getEventsByStatus(
            @PathVariable Event.EventStatus status) {
        
        List<EventDTO> events = eventService.getEventsByStatus(status);
        
        List<EntityModel<EventDTO>> eventResources = events.stream()
                .map(event -> EntityModel.of(event, 
                        linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel()))
                .collect(Collectors.toList());
        
        Link link = linkTo(methodOn(EventController.class).getEventsByStatus(status)).withSelfRel();
        CollectionModel<EntityModel<EventDTO>> result = CollectionModel.of(eventResources, link);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/organizer/{organizerId}")
    @Operation(summary = "Get events by organizer", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<CollectionModel<EntityModel<EventDTO>>> getEventsByOrganizer(
            @PathVariable Long organizerId) {
        
        List<EventDTO> events = eventService.getEventsByOrganizer(organizerId);
        
        List<EntityModel<EventDTO>> eventResources = events.stream()
                .map(event -> EntityModel.of(event, 
                        linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel()))
                .collect(Collectors.toList());
        
        Link link = linkTo(methodOn(EventController.class).getEventsByOrganizer(organizerId)).withSelfRel();
        CollectionModel<EntityModel<EventDTO>> result = CollectionModel.of(eventResources, link);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get events by venue", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<CollectionModel<EntityModel<EventDTO>>> getEventsByVenue(
            @PathVariable Long venueId) {
        
        List<EventDTO> events = eventService.getEventsByVenue(venueId);
        
        List<EntityModel<EventDTO>> eventResources = events.stream()
                .map(event -> EntityModel.of(event, 
                        linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel()))
                .collect(Collectors.toList());
        
        Link link = linkTo(methodOn(EventController.class).getEventsByVenue(venueId)).withSelfRel();
        CollectionModel<EntityModel<EventDTO>> result = CollectionModel.of(eventResources, link);
        
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> createEvent(@Valid @RequestBody EventCreateRequest request) {
        EventDTO createdEvent = eventService.createEvent(request);
        
        EntityModel<EventDTO> resource = EntityModel.of(createdEvent,
                linkTo(methodOn(EventController.class).getEventById(createdEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventController.class).getAllEvents()).withRel("all_events"));
        
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest request) {
        
        EventDTO updatedEvent = eventService.updateEvent(id, request);
        
        EntityModel<EventDTO> resource = EntityModel.of(updatedEvent,
                linkTo(methodOn(EventController.class).getEventById(updatedEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventController.class).getAllEvents()).withRel("all_events"));
        
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publish an event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> publishEvent(@PathVariable Long id) {
        EventDTO publishedEvent = eventService.publishEvent(id);
        
        EntityModel<EventDTO> resource = EntityModel.of(publishedEvent,
                linkTo(methodOn(EventController.class).getEventById(publishedEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventController.class).getAllEvents()).withRel("all_events"));
        
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}