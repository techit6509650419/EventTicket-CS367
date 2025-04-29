package com.eventticket.organizer.controller;

import com.eventticket.organizer.dto.BookingRequest;
import com.eventticket.organizer.dto.EventDTO;
import com.eventticket.organizer.dto.response.StatusUpdateResponse;
import com.eventticket.organizer.dto.response.TicketStatisticsResponse;
import com.eventticket.organizer.exception.ResourceNotFoundException;
import com.eventticket.organizer.service.EventManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Management API", description = "Endpoints for managing event operations")
public class EventManagementController {

    private final EventManagementService eventManagementService;

    @PutMapping("/{id}/postpone")
    @Operation(summary = "Postpone an event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> postponeEvent(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
            @RequestParam String newTime,
            @RequestParam @NotBlank String reason) {
        
        EventDTO event = eventManagementService.getEventById(id);
        EventDTO postponedEvent = eventManagementService.postponeEvent(id, newDate, newTime, reason);
        
        EntityModel<EventDTO> resource = EntityModel.of(postponedEvent,
                linkTo(methodOn(EventController.class).getEventById(postponedEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventManagementController.class).getEventStatusUpdates(id)).withRel("status_updates"));
        
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an event", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> cancelEvent(
            @PathVariable Long id,
            @RequestParam @NotBlank String reason) {
        
        EventDTO event = eventManagementService.getEventById(id);
        EventDTO cancelledEvent = eventManagementService.cancelEvent(id, reason);
        
        EntityModel<EventDTO> resource = EntityModel.of(cancelledEvent,
                linkTo(methodOn(EventController.class).getEventById(cancelledEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventManagementController.class).getEventStatusUpdates(id)).withRel("status_updates"));
        
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}/update-capacity")
    @Operation(summary = "Update event capacity", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EntityModel<EventDTO>> updateCapacity(
            @PathVariable Long id,
            @RequestParam @Positive Integer additionalCapacity,
            @RequestParam @NotBlank String reason) {
        
        EventDTO event = eventManagementService.getEventById(id);
        EventDTO updatedEvent = eventManagementService.updateCapacity(id, additionalCapacity, reason);
        
        EntityModel<EventDTO> resource = EntityModel.of(updatedEvent,
                linkTo(methodOn(EventController.class).getEventById(updatedEvent.getId())).withSelfRel(),
                linkTo(methodOn(EventManagementController.class).getEventStatusUpdates(id)).withRel("status_updates"));
        
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}/status-updates")
    @Operation(summary = "Get event status updates", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<StatusUpdateResponse> getEventStatusUpdates(@PathVariable Long id) {
        StatusUpdateResponse statusUpdates = eventManagementService.getEventStatusUpdates(id);
        return ResponseEntity.ok(statusUpdates);
    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "Get event statistics", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<TicketStatisticsResponse> getEventStatistics(@PathVariable Long id) {
        TicketStatisticsResponse statistics = eventManagementService.getEventStatistics(id);
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/{id}/reserve-organizer-tickets")
    @Operation(summary = "Reserve tickets for organizer team", security = @SecurityRequirement(name = "JWT"))
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> reserveOrganizerTickets(
            @PathVariable Long id,
            @RequestBody BookingRequest bookingRequest) {

        eventManagementService.reserveOrganizerTickets(
            id,
            bookingRequest.getUserId(),
            bookingRequest.getTicketType(),
            bookingRequest.getQuantity()
        );
        return ResponseEntity.ok().build();
    }

}