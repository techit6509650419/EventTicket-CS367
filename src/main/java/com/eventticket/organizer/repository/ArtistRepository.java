package com.eventticket.organizer.repository;

import com.eventticket.organizer.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Artist> findByNameContainingIgnoreCase(@Param("name") String name);
    
    List<Artist> findByGenre(String genre);
    
    List<Artist> findByCountry(String country);
    
    @Query("SELECT a FROM Artist a JOIN a.events e WHERE e.id = :eventId")
    List<Artist> findByEventId(@Param("eventId") Long eventId);
    
    @Query(value = "SELECT a.* FROM artists a " +
                   "JOIN event_artists ea ON a.id = ea.artist_id " +
                   "GROUP BY a.id " +
                   "ORDER BY COUNT(ea.event_id) DESC " +
                   "LIMIT 10", nativeQuery = true)
    List<Artist> findTopArtists(@Param("limit") int limit);
}