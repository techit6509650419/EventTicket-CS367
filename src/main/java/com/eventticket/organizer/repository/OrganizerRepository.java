package com.eventticket.organizer.repository;

import com.eventticket.organizer.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query("SELECT o FROM Organizer o WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Organizer> findByNameContainingIgnoreCase(@Param("name") String name);
    
    Optional<Organizer> findByTaxId(String taxId);
    
    Optional<Organizer> findByContactEmail(String contactEmail);
    
    @Query(value = "SELECT o.* FROM organizers o " +
                   "JOIN events e ON o.id = e.organizer_id " +
                   "GROUP BY o.id " +
                   "ORDER BY COUNT(e.id) DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Organizer> findTopOrganizers(@Param("limit") int limit);
}