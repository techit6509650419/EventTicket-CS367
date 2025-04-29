package com.eventticket.organizer.controller;

import com.eventticket.organizer.dto.SearchFilterRequest;
import com.eventticket.organizer.dto.response.SearchResponse;
import com.eventticket.organizer.dto.ChatbotFaqRequest;
import com.eventticket.organizer.dto.response.ChatbotResponse;
import com.eventticket.organizer.model.Event;
import com.eventticket.organizer.service.EventSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search/events")
@RequiredArgsConstructor
@Tag(name = "Event Search API", description = "Endpoints for searching events")
public class EventSearchController {

    private final EventSearchService eventSearchService;

    @GetMapping("/filter")
    @Operation(summary = "Search events with filters")
    public ResponseEntity<SearchResponse> searchEvents(
            @ModelAttribute SearchFilterRequest filterRequest) {
        
        SearchResponse searchResults = eventSearchService.searchEvents(filterRequest);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping
    @Operation(summary = "Search events by keyword")
    public ResponseEntity<List<Event>> searchByKeyword(
            @RequestParam String query) {
        
        List<Event> events = eventSearchService.searchByKeyword(query);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending events")
    public ResponseEntity<List<Event>> getTrendingEvents() {
        List<Event> events = eventSearchService.getTrendingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming events")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventSearchService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get events within a date range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Event> events = eventSearchService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get events by category")
    public ResponseEntity<List<Event>> getEventsByCategory(
            @PathVariable String category) {
        
        List<Event> events = eventSearchService.findByCategory(category);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/faqs")
    @Operation(summary = "Get frequently asked questions about an event")
    public ResponseEntity<List<Map<String, Object>>> getEventFaqs(
            @PathVariable Long eventId) {
        
        List<Map<String, Object>> faqs = eventSearchService.getEventFaqs(eventId);
        return ResponseEntity.ok(faqs);
    }

    @PostMapping("/faq")
    @Operation(summary = "Get answers to frequently asked questions")
    public ResponseEntity<ChatbotResponse> getFaqAnswer(@RequestBody Map<String, Object> requestBody) {
        // Extract values from the request body
        String query = (String) requestBody.get("query");
        Long userId = requestBody.get("userId") != null ? Long.valueOf(requestBody.get("userId").toString()) : null;
        String sessionId = (String) requestBody.get("sessionId");
        String eventId = (String) requestBody.get("eventId");

        // Call the service layer
        ChatbotResponse response = eventSearchService.getFaqAnswer(query, userId, sessionId, eventId);
        return ResponseEntity.ok(response);
    }
}