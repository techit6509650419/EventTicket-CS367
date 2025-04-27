package com.eventticket.organizer.repository;

import com.eventticket.organizer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(Event.EventStatus status);
    
    List<Event> findByOrganizerId(Long organizerId);
    
    List<Event> findByVenueId(Long venueId);
    
    @Query("SELECT e FROM Event e WHERE e.date >= :startDate AND e.date <= :endDate")
    List<Event> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e FROM Event e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT e FROM Event e JOIN e.artists a WHERE a.id = :artistId")
    List<Event> findByArtistId(@Param("artistId") Long artistId);
    
    @Query("SELECT e FROM Event e WHERE e.category = :category")
    List<Event> findByCategory(@Param("category") String category);
    
    @Query("SELECT e FROM Event e WHERE e.date >= CURRENT_DATE ORDER BY e.date ASC")
    List<Event> findUpcomingEvents();
    
    @Query(value = "SELECT e.* FROM events e " +
                   "LEFT JOIN ticket_types tt ON e.id = tt.event_id " +
                   "WHERE e.status = 'UPCOMING' " +
                   "GROUP BY e.id " +
                   "ORDER BY COUNT(tt.id) DESC " +
                   "LIMIT 10", nativeQuery = true)
    List<Event> findTrendingEvents();
}