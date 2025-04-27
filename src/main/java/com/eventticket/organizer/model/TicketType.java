package com.eventticket.organizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private Event event;

    @NotBlank
    private String type;

    @NotNull
    @Positive
    private BigDecimal price;

    @Positive
    private Integer quantity;

    @Positive
    private Integer maxPerPurchase;

    private LocalDateTime saleStartTime;

    private LocalDateTime saleEndTime;

    @ElementCollection
    @CollectionTable(name = "ticket_benefits", joinColumns = @JoinColumn(name = "ticket_type_id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}