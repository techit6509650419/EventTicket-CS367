package com.eventticket.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString(exclude = {"artists", "ticketTypes", "statusUpdates"})
@EqualsAndHashCode(exclude = {"artists", "ticketTypes", "statusUpdates"})
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    @NotNull
    private Venue venue;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String time;

    @Positive
    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    @NotNull
    private Organizer organizer;

    @NotBlank
    private String category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_artists",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>();

    @NotBlank
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<TicketType> ticketTypes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<EventStatusUpdate> statusUpdates = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum EventStatus {
        DRAFT,
        UPCOMING,
        ONGOING,
        COMPLETED,
        POSTPONED,
        CANCELLED
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void removeArtist(Artist artist) {
        this.artists.remove(artist);
    }

    public void addTicketType(TicketType ticketType) {
        ticketType.setEvent(this);
        this.ticketTypes.add(ticketType);
    }

    public void removeTicketType(TicketType ticketType) {
        this.ticketTypes.remove(ticketType);
        ticketType.setEvent(null);
    }

    public void addStatusUpdate(EventStatusUpdate statusUpdate) {
        statusUpdate.setEvent(this);
        this.statusUpdates.add(statusUpdate);
    }
}