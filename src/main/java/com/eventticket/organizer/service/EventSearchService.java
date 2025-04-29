package com.eventticket.organizer.service;

import com.eventticket.organizer.dto.SearchFilterRequest;
import com.eventticket.organizer.dto.response.SearchResponse;
import com.eventticket.organizer.dto.response.ChatbotResponse;
import com.eventticket.organizer.model.Artist;
import com.eventticket.organizer.model.Event;
import com.eventticket.organizer.model.TicketType;
import com.eventticket.organizer.repository.ArtistRepository;
import com.eventticket.organizer.repository.EventRepository;
import com.eventticket.organizer.repository.VenueRepository;
import com.eventticket.organizer.service.client.TicketServiceClient;
import com.eventticket.organizer.dto.ChatbotFaqRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventSearchService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final TicketServiceClient ticketServiceClient;
    
    @PersistenceContext
    private EntityManager entityManager;

    public SearchResponse searchEvents(SearchFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Add keyword search
        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
            Predicate namePredicate = cb.like(cb.lower(eventRoot.get("name")), keyword);
            Predicate descriptionPredicate = cb.like(cb.lower(eventRoot.get("description")), keyword);
            predicates.add(cb.or(namePredicate, descriptionPredicate));
        }
        
        // Add category filter
        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            predicates.add(cb.equal(eventRoot.get("category"), filter.getCategory()));
        }
        
        // Add venue filter
        if (filter.getVenueId() != null) {
            Join<Event, Object> venueJoin = eventRoot.join("venue");
            predicates.add(cb.equal(venueJoin.get("id"), filter.getVenueId()));
        } else if (filter.getVenueName() != null && !filter.getVenueName().isEmpty()) {
            Join<Event, Object> venueJoin = eventRoot.join("venue");
            predicates.add(cb.like(cb.lower(venueJoin.get("name")), "%" + filter.getVenueName().toLowerCase() + "%"));
        }
        
        // Add artist filter
        if (filter.getArtistId() != null) {
            Join<Event, Artist> artistJoin = eventRoot.join("artists");
            predicates.add(cb.equal(artistJoin.get("id"), filter.getArtistId()));
        } else if (filter.getArtistName() != null && !filter.getArtistName().isEmpty()) {
            Join<Event, Artist> artistJoin = eventRoot.join("artists");
            predicates.add(cb.like(cb.lower(artistJoin.get("name")), "%" + filter.getArtistName().toLowerCase() + "%"));
        }
        
        // Add date range filter
        if (filter.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("date"), filter.getDateFrom()));
        }
        
        if (filter.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(eventRoot.get("date"), filter.getDateTo()));
        }
        
        // Add status filter
        if (filter.getStatus() != null) {
            predicates.add(cb.equal(eventRoot.get("status"), filter.getStatus()));
        }
        
        // Add organizer filter
        if (filter.getOrganizerId() != null) {
            Join<Event, Object> organizerJoin = eventRoot.join("organizer");
            predicates.add(cb.equal(organizerJoin.get("id"), filter.getOrganizerId()));
        } else if (filter.getOrganizerName() != null && !filter.getOrganizerName().isEmpty()) {
            Join<Event, Object> organizerJoin = eventRoot.join("organizer");
            predicates.add(cb.like(cb.lower(organizerJoin.get("name")), "%" + filter.getOrganizerName().toLowerCase() + "%"));
        }
        
        // Add price range filter
        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            Join<Event, TicketType> ticketTypeJoin = eventRoot.join("ticketTypes");
            
            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(ticketTypeJoin.get("price"), new BigDecimal(filter.getMinPrice())));
            }
            
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(ticketTypeJoin.get("price"), new BigDecimal(filter.getMaxPrice())));
            }
        }
        
        // Add city filter
        if (filter.getCity() != null && !filter.getCity().isEmpty()) {
            Join<Event, Object> venueJoin = eventRoot.join("venue");
            predicates.add(cb.equal(cb.lower(venueJoin.get("city")), filter.getCity().toLowerCase()));
        }
        
        // Add country filter
        if (filter.getCountry() != null && !filter.getCountry().isEmpty()) {
            Join<Event, Object> venueJoin = eventRoot.join("venue");
            predicates.add(cb.equal(cb.lower(venueJoin.get("country")), filter.getCountry().toLowerCase()));
        }
        
        // Apply predicates
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // Apply sorting
        if (filter.getSortBy() != null && !filter.getSortBy().isEmpty()) {
            if ("date".equals(filter.getSortBy())) {
                if ("desc".equalsIgnoreCase(filter.getSortDirection())) {
                    query.orderBy(cb.desc(eventRoot.get("date")));
                } else {
                    query.orderBy(cb.asc(eventRoot.get("date")));
                }
            } else if ("name".equals(filter.getSortBy())) {
                if ("desc".equalsIgnoreCase(filter.getSortDirection())) {
                    query.orderBy(cb.desc(eventRoot.get("name")));
                } else {
                    query.orderBy(cb.asc(eventRoot.get("name")));
                }
            }
            // Add more sorting options as needed
        } else {
            // Default sort by date ascending
            query.orderBy(cb.asc(eventRoot.get("date")));
        }
        
        // Execute query with pagination
        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;
        
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        
        // Count total results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Event> countRoot = countQuery.from(Event.class);
        countQuery.select(cb.count(countRoot));
        
        // Apply the same predicates to count query
        if (!predicates.isEmpty()) {
            countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        
        // Map results to response
        SearchResponse response = new SearchResponse();
        
        for (Event event : events) {
            SearchResponse.EventSummary summary = mapEventToSummary(event);
            response.getResults().add(summary);
        }
        
        response.setTotalResults(totalCount.intValue());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages((int) Math.ceil((double) totalCount / size));
        response.setSearchParams(filter);
        
        return response;
    }

    public List<Event> getTrendingEvents() {
        return eventRepository.findTrendingEvents();
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents();
    }

    public List<Event> searchByKeyword(String keyword) {
        return eventRepository.searchByKeyword(keyword);
    }

    public List<Event> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByDateRange(startDate, endDate);
    }

    public List<Event> findByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Map<String, Object>> getEventFaqs(Long eventId) {
        return ticketServiceClient.getFrequentlyAskedQuestions(eventId);
    }

    public ChatbotResponse getFaqAnswer(ChatbotFaqRequest request) {
        return ticketServiceClient.getFaqAnswer(request);
    }

    public ChatbotResponse getFaqAnswer(String query, Long userId, String sessionId, String eventId) {
        // Create a ChatbotFaqRequest object manually
        ChatbotFaqRequest request = new ChatbotFaqRequest(query, userId, sessionId, eventId);

        // Call the TicketServiceClient
        return ticketServiceClient.getFaqAnswer(request);
    }

    private SearchResponse.EventSummary mapEventToSummary(Event event) {
        SearchResponse.EventSummary summary = new SearchResponse.EventSummary();
        summary.setId(event.getId());
        summary.setName(event.getName());
        summary.setDate(event.getDate());
        summary.setTime(event.getTime());
        summary.setVenue(event.getVenue().getName());
        summary.setCategory(event.getCategory());
        
        List<String> artistNames = event.getArtists().stream()
                .map(Artist::getName)
                .collect(Collectors.toList());
        summary.setArtists(artistNames);
        
        summary.setStatus(event.getStatus());
        summary.setImageUrl(event.getImageUrl());
        
        // Calculate ticket price range
        if (!event.getTicketTypes().isEmpty()) {
            BigDecimal minPrice = event.getTicketTypes().stream()
                    .map(TicketType::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            
            BigDecimal maxPrice = event.getTicketTypes().stream()
                    .map(TicketType::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            
            SearchResponse.TicketPriceRange priceRange = new SearchResponse.TicketPriceRange();
            priceRange.setMin(minPrice);
            priceRange.setMax(maxPrice);
            summary.setTicketPrice(priceRange);
        }
        
        // Set availability info (simplified for example)
        int totalTickets = event.getTicketTypes().stream()
                .mapToInt(TicketType::getQuantity)
                .sum();
        
        // In a real scenario, this would come from the ticket service
        int ticketsLeft = (int) (totalTickets * 0.7); // Assuming 30% sold
        summary.setTicketsLeft(ticketsLeft);
        
        if (ticketsLeft < totalTickets * 0.2) {
            summary.setAvailability("low");
        } else if (ticketsLeft < totalTickets * 0.6) {
            summary.setAvailability("medium");
        } else {
            summary.setAvailability("high");
        }
        
        return summary;
    }
}