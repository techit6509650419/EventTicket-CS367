package com.eventticket.organizer.service.client;

import com.eventticket.organizer.dto.response.TicketStatisticsResponse;
import com.eventticket.organizer.dto.response.BookingResponse;
import com.eventticket.organizer.dto.response.ChatbotResponse;
import com.eventticket.organizer.dto.ChatbotFaqRequest;
import com.eventticket.organizer.exception.ServiceCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ticket.service.url}")
    private String ticketServiceUrl;

    @Value("${ticket.service.api-key}")
    private String apiKey;

    public TicketStatisticsResponse getTicketStatistics(Long eventId) {
        try {
            String url = ticketServiceUrl + "/api/tickets/event/" + eventId + "/statistics";
            HttpHeaders headers = createHeaders();
            
            ResponseEntity<TicketStatisticsResponse> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    new HttpEntity<>(headers), 
                    TicketStatisticsResponse.class);
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error retrieving ticket statistics for eventId: {}", eventId, e);
            throw new ServiceCommunicationException("Could not retrieve ticket statistics: " + e.getMessage());
        }
    }

    public boolean reserveOrganizerTickets(Long eventId, Long organizerId, String ticketType, int quantity) {
        try {
            String url = ticketServiceUrl + "/api/bookings";
            HttpHeaders headers = createHeaders();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("eventId", eventId);
            requestBody.put("userId", organizerId);
            requestBody.put("ticketType", ticketType);
            requestBody.put("quantity", quantity);
            requestBody.put("note", "Reserved for organizer team");
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, 
                    HttpMethod.POST, 
                    new HttpEntity<>(requestBody, headers), 
                    Map.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            log.error("Error reserving organizer tickets for eventId: {}", eventId, e);
            throw new ServiceCommunicationException("Could not reserve organizer tickets: " + e.getMessage());
        }
    }

    public BookingResponse reserveOrganizerTicketsAndGetResponse(String eventId, Long userId, String ticketType, Integer quantity, String note) {
        try {
            String url = ticketServiceUrl + "/api/bookings";
            HttpHeaders headers = createHeaders();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("eventId", eventId);
            requestBody.put("userId", userId);
            requestBody.put("ticketType", ticketType);
            requestBody.put("quantity", quantity);
            requestBody.put("note", note);

            log.info("Booking request body: {}", requestBody);

            ResponseEntity<BookingResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    BookingResponse.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error reserving organizer tickets (with response) for eventId: {}", eventId, e);
            throw new ServiceCommunicationException("Could not reserve organizer tickets: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getFrequentlyAskedQuestions(Long eventId) {
        try {
            String url = ticketServiceUrl + "/api/chatbot/faq";
            HttpHeaders headers = createHeaders();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", "common questions");
            requestBody.put("eventId", eventId.toString());
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, 
                    HttpMethod.POST, 
                    new HttpEntity<>(requestBody, headers), 
                    Map.class);
            
            Map<String, Object> responseBody = response.getBody();
            return (List<Map<String, Object>>) responseBody.get("relatedFaq");
        } catch (RestClientException e) {
            log.error("Error retrieving FAQs for eventId: {}", eventId, e);
            throw new ServiceCommunicationException("Could not retrieve FAQs: " + e.getMessage());
        }
    }

    public ChatbotResponse getFaqAnswer(ChatbotFaqRequest request) {
        try {
            String url = ticketServiceUrl + "/api/chatbot/faq";
            HttpHeaders headers = createHeaders();

            ResponseEntity<ChatbotResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    ChatbotResponse.class
            );

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error calling chatbot service for FAQ", e);
            throw new ServiceCommunicationException("Could not retrieve FAQ answers: " + e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }
}