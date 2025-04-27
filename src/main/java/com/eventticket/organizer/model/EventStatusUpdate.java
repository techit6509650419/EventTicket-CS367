package com.eventticket.organizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "event_status_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private Event event;

    @NotBlank
    private String field;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @ElementCollection
    @CollectionTable(name = "status_update_notifications", 
                     joinColumns = @JoinColumn(name = "update_id"))
    @MapKeyColumn(name = "notification_type")
    @Column(name = "sent")
    private Map<String, Boolean> notifications = new HashMap<>();

    @CreationTimestamp
    private LocalDateTime timestamp;

    public enum NotificationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}