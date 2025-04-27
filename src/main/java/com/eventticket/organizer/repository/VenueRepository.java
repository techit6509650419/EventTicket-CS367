package com.eventticket.organizer.repository;

import com.eventticket.organizer.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("SELECT v FROM Venue v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Venue> findByNameContainingIgnoreCase(@Param("name") String name);
    
    List<Venue> findByCity(String city);
    
    List<Venue> findByCountry(String country);
    
    @Query("SELECT v FROM Venue v WHERE v.capacity >= :minCapacity")
    List<Venue> findByMinCapacity(@Param("minCapacity") Integer minCapacity);
    
    @Query("SELECT v FROM Venue v WHERE v.capacity <= :maxCapacity")
    List<Venue> findByMaxCapacity(@Param("maxCapacity") Integer maxCapacity);
    
    @Query("SELECT v FROM Venue v WHERE v.capacity >= :minCapacity AND v.capacity <= :maxCapacity")
    List<Venue> findByCapacityRange(@Param("minCapacity") Integer minCapacity, @Param("maxCapacity") Integer maxCapacity);
}