package com.eventticket.organizer.service;

import com.eventticket.organizer.dto.EventDTO;
import com.eventticket.organizer.dto.response.StatusUpdateResponse;
import com.eventticket.organizer.dto.response.TicketStatisticsResponse;
import com.eventticket.organizer.exception.BusinessException;
import com.eventticket.organizer.exception.ResourceNotFoundException;
import com.eventticket.organizer.model.Event;
import com.eventticket.organizer.model.EventStatusUpdate;
import com.eventticket.organizer.repository.EventRepository;
import com.eventticket.organizer.service.client.TicketServiceClient;
import com.eventticket.organizer.util.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventManagementService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final TicketServiceClient ticketServiceClient;

    @Transactional
    public EventDTO postponeEvent(Long id, LocalDate newDate, String newTime, String reason) {
        Event event = findEventById(id);
        
        if (event.getStatus() == Event.EventStatus.COMPLETED || event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new BusinessException("Cannot postpone a completed or cancelled event");
        }
        
        LocalDate oldDate = event.getDate();
        String oldTime = event.getTime();
        
        event.setDate(newDate);
        event.setTime(newTime);
        event.setStatus(Event.EventStatus.POSTPONED);
        
        EventStatusUpdate statusUpdate = new EventStatusUpdate();
        statusUpdate.setField("date_time");
        statusUpdate.setOldValue(oldDate + " " + oldTime);
        statusUpdate.setNewValue(newDate + " " + newTime);
        statusUpdate.setReason(reason);
        statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
        
        // Initialize notification tracking
        Map<String, Boolean> notifications = new HashMap<>();
        notifications.put("email", false);
        notifications.put("sms", false);
        notifications.put("push", false);
        statusUpdate.setNotifications(notifications);
        
        event.addStatusUpdate(statusUpdate);
        
        Event updatedEvent = eventRepository.save(event);
        
        // Trigger notification to ticket holders would happen here
        // This is just a placeholder for the actual notification logic
        log.info("Event with ID {} postponed. Notification should be sent to ticket holders.", id);
        
        return eventMapper.toDto(updatedEvent);
    }

    @Transactional
    public EventDTO cancelEvent(Long id, String reason) {
        Event event = findEventById(id);
        
        if (event.getStatus() == Event.EventStatus.COMPLETED || event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel a completed or already cancelled event");
        }
        
        Event.EventStatus oldStatus = event.getStatus();
        event.setStatus(Event.EventStatus.CANCELLED);
        
        EventStatusUpdate statusUpdate = new EventStatusUpdate();
        statusUpdate.setField("status");
        statusUpdate.setOldValue(oldStatus.toString());
        statusUpdate.setNewValue(Event.EventStatus.CANCELLED.toString());
        statusUpdate.setReason(reason);
        statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
        
        // Initialize notification tracking
        Map<String, Boolean> notifications = new HashMap<>();
        notifications.put("email", false);
        notifications.put("sms", false);
        notifications.put("push", false);
        statusUpdate.setNotifications(notifications);
        
        event.addStatusUpdate(statusUpdate);
        
        Event updatedEvent = eventRepository.save(event);
        
        // Trigger notification to ticket holders would happen here
        // This is just a placeholder for the actual notification logic
        log.info("Event with ID {} cancelled. Notification should be sent to ticket holders.", id);
        
        return eventMapper.toDto(updatedEvent);
    }

    @Transactional
    public EventDTO updateCapacity(Long id, Integer additionalCapacity, String reason) {
        Event event = findEventById(id);
        
        if (event.getStatus() == Event.EventStatus.COMPLETED || event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new BusinessException("Cannot update capacity for a completed or cancelled event");
        }
        
        Integer oldCapacity = event.getVenue().getCapacity();
        Integer newCapacity = oldCapacity + additionalCapacity;
        
        if (newCapacity <= 0) {
            throw new BusinessException("New capacity must be greater than 0");
        }
        
        event.getVenue().setCapacity(newCapacity);
        
        EventStatusUpdate statusUpdate = new EventStatusUpdate();
        statusUpdate.setField("capacity");
        statusUpdate.setOldValue(oldCapacity.toString());
        statusUpdate.setNewValue(newCapacity.toString());
        statusUpdate.setReason(reason);
        statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.PENDING);
        
        event.addStatusUpdate(statusUpdate);
        
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }

    public StatusUpdateResponse getEventStatusUpdates(Long id) {
        Event event = findEventById(id);
        return eventMapper.toStatusUpdateResponse(event);
    }

    public TicketStatisticsResponse getEventStatistics(Long id) {
        // Check if event exists
        Event event = findEventById(id);
        
        // Call ticket service to get statistics
        return ticketServiceClient.getTicketStatistics(id);
    }

    public EventDTO getEventById(Long id) {
        Event event = findEventById(id);
        return eventMapper.toDto(event);
    }

    @Transactional
    public void reserveOrganizerTickets(Long eventId, Long organizerId, String ticketType, int quantity) {
        boolean reserved = ticketServiceClient.reserveOrganizerTickets(eventId, organizerId, ticketType, quantity);

        if (!reserved) {
            throw new BusinessException("Failed to reserve organizer tickets");
        }

        log.info("Successfully reserved {} {} tickets for organizer {} for event {}", 
                quantity, ticketType, organizerId, eventId);
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void updateEventStatuses() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Find all upcoming events
        List<Event> upcomingEvents = eventRepository.findByStatus(Event.EventStatus.UPCOMING);
        for (Event event : upcomingEvents) {
            if (event.getDate().equals(today)) {
                String[] timeParts = event.getTime().split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                LocalDateTime eventStartTime = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), hour, minute);
                LocalDateTime eventEndTime = eventStartTime.plusMinutes(event.getDuration());

                if (now.isAfter(eventStartTime) && now.isBefore(eventEndTime)) {
                    event.setStatus(Event.EventStatus.ONGOING);
                    EventStatusUpdate statusUpdate = new EventStatusUpdate();
                    statusUpdate.setField("status");
                    statusUpdate.setOldValue(Event.EventStatus.UPCOMING.toString());
                    statusUpdate.setNewValue(Event.EventStatus.ONGOING.toString());
                    statusUpdate.setReason("Event started");
                    statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.COMPLETED);
                    event.addStatusUpdate(statusUpdate);
                    eventRepository.save(event);
                    log.info("Updated event {} status to ONGOING", event.getId());
                } else if (now.isAfter(eventEndTime)) {
                    event.setStatus(Event.EventStatus.COMPLETED);
                    EventStatusUpdate statusUpdate = new EventStatusUpdate();
                    statusUpdate.setField("status");
                    statusUpdate.setOldValue(Event.EventStatus.ONGOING.toString());
                    statusUpdate.setNewValue(Event.EventStatus.COMPLETED.toString());
                    statusUpdate.setReason("Event completed");
                    statusUpdate.setNotificationStatus(EventStatusUpdate.NotificationStatus.COMPLETED);
                    event.addStatusUpdate(statusUpdate);
                    eventRepository.save(event);
                    log.info("Updated event {} status to COMPLETED", event.getId());
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    @Transactional
    public void processStatusUpdateNotifications() {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            for (EventStatusUpdate update : event.getStatusUpdates()) {
                if (update.getNotificationStatus() == EventStatusUpdate.NotificationStatus.PENDING) {
                    // Send notifications logic would go here
                    // This is just a placeholder
                    log.info("Processing notification for event {} status update: {}", event.getId(), update.getField());
                    // Update notification status
                    update.getNotifications().put("email", true);
                    update.getNotifications().put("sms", true);
                    update.getNotifications().put("push", true);
                    update.setNotificationStatus(EventStatusUpdate.NotificationStatus.COMPLETED);
                }
            }
        }
        eventRepository.saveAll(events);
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
}