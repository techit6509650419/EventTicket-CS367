package com.eventticket.organizer.controller;

import com.eventticket.organizer.dto.ChatbotFaqRequest;
import com.eventticket.organizer.dto.response.ChatbotResponse;
import com.eventticket.organizer.dto.response.TicketStatisticsResponse;
import com.eventticket.organizer.dto.response.BookingResponse;
import com.eventticket.organizer.service.client.TicketServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebTicketController {

    private final TicketServiceClient ticketServiceClient;

    // Home
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Ticket Statistics
    @GetMapping("/web/ticket-statistics")
    public String ticketStatisticsForm() {
        return "ticket-statistics-form";
    }

    @PostMapping("/web/ticket-statistics")
    public String ticketStatisticsResult(@RequestParam Long eventId, Model model) {
        TicketStatisticsResponse stats = ticketServiceClient.getTicketStatistics(eventId);
        model.addAttribute("stats", stats);
        return "ticket-statistics-result";
    }

    // Chatbot FAQ
    @GetMapping("/web/faq")
    public String faqForm() {
        return "faq-form";
    }

    @PostMapping("/web/faq")
    public String faqResult(@RequestParam String query,
                            @RequestParam(required = false) Long userId,
                            @RequestParam String sessionId,
                            @RequestParam(required = false) String eventId,
                            Model model) {
        if (userId == null) {
            model.addAttribute("error", "User ID is required.");
            return "faq-form";
        }
        ChatbotFaqRequest req = new ChatbotFaqRequest();
        req.setQuery(query);
        req.setUserId(userId);
        req.setSessionId(sessionId);
        req.setEventId((eventId != null && !eventId.trim().isEmpty()) ? eventId : null);
        ChatbotResponse response = ticketServiceClient.getFaqAnswer(req);
        model.addAttribute("faq", response);
        return "faq-result";
    }

    // Reserve Organizer Tickets
    @GetMapping("/web/reserve-organizer-tickets")
    public String reserveOrganizerTicketsForm() {
        return "reserve-organizer-tickets";
    }

    @PostMapping("/web/reserve-organizer-tickets")
    public String reserveOrganizerTicketsResult(@RequestParam String eventId,
                                               @RequestParam Long userId,
                                               @RequestParam String ticketType,
                                               @RequestParam Integer quantity,
                                               @RequestParam(required = false) String note,
                                               Model model) {
        BookingResponse booking = ticketServiceClient.reserveOrganizerTicketsAndGetResponse(
                eventId, userId, ticketType, quantity, note
        );
        model.addAttribute("booking", booking);
        return "reserve-organizer-tickets-result";
    }
}
