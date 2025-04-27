package com.eventticket.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeDTO {

    private Long id;
    
    @NotBlank(message = "Ticket type is required")
    private String type;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @Positive(message = "Max per purchase must be positive")
    private Integer maxPerPurchase;
    
    private LocalDateTime saleStartTime;
    
    private LocalDateTime saleEndTime;
    
    private List<String> benefits = new ArrayList<>();
}